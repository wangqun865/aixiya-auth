package aixiya.framework.backend.platform.auth.controller;

import aixiya.framework.backend.platform.auth.common.RedisConstants;
import aixiya.framework.backend.platform.auth.common.RestCodeConstants;
import aixiya.framework.backend.platform.auth.configure.SSOConfiguration;
import aixiya.framework.backend.platform.auth.configure.UserAuthConfig;
import aixiya.framework.backend.platform.auth.configure.UserAuthUtil;
import aixiya.framework.backend.platform.auth.controller.form.AuthenticateJson;
import aixiya.framework.backend.platform.auth.controller.form.LoginForm;
import aixiya.framework.backend.platform.auth.controller.form.LoginOrgForm;
import aixiya.framework.backend.platform.auth.controller.form.LoginRequest;
import aixiya.framework.backend.platform.auth.exception.UserInvalidException;
import aixiya.framework.backend.platform.auth.service.SsoService;
import aixiya.framework.backend.platform.auth.service.ValidateCodeService;
import aixiya.framework.backend.platform.auth.util.IJWTInfo;
import aixiya.framework.backend.platform.user.api.UserAuthClient;
import aixiya.framework.backend.platform.user.api.vo.OrganizationVO;
import aixiya.framework.backend.platform.user.api.vo.UserCheckLoginVO;
import com.aixiya.framework.backend.common.api.AixiyaFwResponse;
import com.aixiya.framework.backend.common.exception.AixiyaFwException;
import com.aixiya.framework.backend.redis.starter.service.RedisService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;


/**
 * @Author  wangqun865@163.com
 */

@Slf4j
@RestController
@RequestMapping("public/oauth")
@RequiredArgsConstructor
public class OauthController {

	private static final String LOCK_USER = "aixiya_lock_user:";

	private static final String PASSWORD_ERROR_COUNT = "aixiya_password_error_count:";

	@Value("${jwt.token-header}")
	private String tokenHeader;

	@Autowired
	private RedisService redisService;
	
    @Autowired
    private SsoService ssoService;

    @Autowired
	private UserAuthUtil userAuthUtil;

    @Autowired
    private UserAuthConfig userAuthConfig;

	@Autowired
	private SSOConfiguration ssoConfiguration;

	@Autowired
	private ValidateCodeService validateCodeService;

	@Autowired
	private UserAuthClient userAuthClient;

	@RequestMapping(value = "login", method = RequestMethod.POST)
    public AixiyaFwResponse login(
    		@Valid @RequestBody LoginRequest loginRequest) throws Exception {
		String request = JSON.toJSONString(loginRequest);
		log.info("public/oauth/login---reuqest param {}" ,request);
		validateCodeService.check(loginRequest.getCaptcha_key() , loginRequest.getCaptcha_value());

		/**
		 * 10分钟内6次密码输入错误 锁定账号 30分钟
		 */
		String userName = loginRequest.getUser_login_name();
		Object userLock = redisService.get(RedisConstants.LOCK_USER + userName);
		if (userLock != null) {
			throw new UserInvalidException("用户已被锁定,请稍后再试!");
		}

		// todo yours business begin
		UserCheckLoginVO userCheckLoginVO = userAuthClient.checkLogin(loginRequest.getUser_login_name(),loginRequest.getUser_login_pwd(),
				loginRequest.getOrga_id() == null ? null : Long.valueOf(loginRequest.getOrga_id()));
		String checkResponse = JSON.toJSONString(userCheckLoginVO);
		log.info("Api:userClient/auth/checkLogin---response param {}" , checkResponse);
		if (userCheckLoginVO == null) {
			log.error("login--> userCheckLoginVO为空");
			throw new UserInvalidException("登陆失败！");
		}
		/**
		 * 10分钟内6次密码输入错误 锁定账号 30分钟
		 */
		if (!userCheckLoginVO.getIfSuccess()) {
			if (userCheckLoginVO.getErrorMessage() != null && userCheckLoginVO.getErrorMessage().equals("用户不存在或密码错误")) {
				Object errorCount = redisService.get(RedisConstants.PASSWORD_ERROR_COUNT + userName);
				if (errorCount != null ) {
					int errorCountInt = (Integer) errorCount;
					if (errorCountInt == 5) {
						redisService.set(RedisConstants.LOCK_USER + userName , new Date() , 1L * 60 *30);
						redisService.del(RedisConstants.PASSWORD_ERROR_COUNT + userName);
						userCheckLoginVO.setErrorMessage("用户不存在或密码错误,用户已被锁定,请稍后再试!");
					} else {
						redisService.incr(RedisConstants.PASSWORD_ERROR_COUNT + userName,1L);
					}
				} else {
					redisService.set(RedisConstants.PASSWORD_ERROR_COUNT + userName ,1L ,1L * 60 *10);
				}
			}
			throw new UserInvalidException(userCheckLoginVO.getErrorMessage());
		}
		redisService.del(RedisConstants.PASSWORD_ERROR_COUNT + userName);


		List<OrganizationVO> orgs = userCheckLoginVO.getOrganizationForFeigns();
		if (orgs != null && orgs.size() != 0 ) {
			if (orgs.size() > 1) {
				LoginForm loginForm = new LoginForm();
				loginForm.setOrgaList(createOrgs(orgs));
				AixiyaFwResponse r =new AixiyaFwResponse().data(RestCodeConstants.LOGIN_ORG_INVALID_CODE ,loginForm,"请选择组织");
				return r;
			}
		} else {
			log.error("login--> orgs为空");
			throw new UserInvalidException("登陆失败！");
		}
        // todo yours business end
		String jwt = "";
		while (true) {
			jwt = ssoService.login(userCheckLoginVO);
			boolean flag = redisService.hasKey(RedisConstants.JWT + jwt);
			if (!flag) {
				break;
			}
		}

		String l = ssoConfiguration.getJwtExpire();
		Long expire = Long.valueOf(l);

        boolean flag = redisService.hset(RedisConstants.JWT + jwt , "null","null",expire);

        if (!flag) {
        	log.error("login--> redis hset fail : jwt :" +  RedisConstants.JWT + jwt );
            String message = "登陆失败!";
            throw new AixiyaFwException(message);
        }

		LoginForm loginForm = new LoginForm();
		loginForm.setBcauthJwt(jwt);
		loginForm.setOrgaList(createOrgs(orgs));

        return new AixiyaFwResponse().data(loginForm);
    }





	@RequestMapping(value = "encode", method = RequestMethod.GET)
	public String encode(String password) throws Exception {
		BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String p = encoder.encode(password);

		return p;
	}


    /**
     * 校验JWT/组织和产品关系
     *
     */
	@RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    @ResponseBody
	public AixiyaFwResponse authenticate(@RequestParam(value = "clientId",required = true)  String clientId, HttpServletRequest request) throws Exception {
        String jwt = request.getHeader(userAuthConfig.getTokenHeader());
        log.info("/authenticate--> request param -> jwt: " + jwt  +"clientId ->" + clientId);
        IJWTInfo info = userAuthUtil.getInfoFromToken(jwt);
        boolean flag = redisService.hasKey(RedisConstants.JWT + jwt);
        if (!flag) {
            log.error("/authenticate--> error jwt:"  + jwt);
            throw new AixiyaFwException("登陆跳转失败");
        }

		Long e = redisService.getExpire(RedisConstants.JWT + jwt);
        if(e < 0) {
        	log.error("/authenticate--> jwt no match :" + jwt);
			throw new AixiyaFwException("登陆跳转失败！");
		}

        String orgId = info.getOrgId();

		AuthenticateJson authenticateJson  = AuthenticateJson.parseAuthenticateJson(info);
		authenticateJson.setJwt(jwt);
		authenticateJson.setClientId(clientId);
		if (StringUtils.isEmpty(info.getManager())) {
			throw new AixiyaFwException("用户管理员为空");
		}
		authenticateJson.setManager(info.getManager());
		authenticateJson.setOrgName(info.getOrgName());
		authenticateJson.setOrgId(info.getOrgId());
		authenticateJson.setUserId(info.getId());
		authenticateJson.setName(info.getName());
		authenticateJson.setLoginName(info.getLoginName());
		String value = JSONObject.toJSONString(authenticateJson);
		String base64encodedString = Base64.getEncoder().encodeToString(value.getBytes());
		String code = UUID.randomUUID().toString();
		Long expire = Long.valueOf(ssoConfiguration.getCodeExpire());
		boolean setFlag = redisService.set(RedisConstants.CODE + code , base64encodedString,expire);
		if (!setFlag) {
			log.error("/authenticate--> jwt set error :" + jwt);
			throw new AixiyaFwException("登陆跳转失败！");
		}
		//最后续期
		Long renewal = Long.valueOf(ssoConfiguration.getRenewal());
		if (e < renewal) {
			redisService.expire(RedisConstants.JWT + jwt ,renewal);
		}
		AixiyaFwResponse aixiyaFwResponse = new AixiyaFwResponse();
		return aixiyaFwResponse.data(code);

    }


    private List<LoginOrgForm> createOrgs(List<OrganizationVO> orgs) {
    	List<LoginOrgForm> list = new ArrayList<>();
		orgs.forEach(org -> {
			LoginOrgForm loginOrgForm = new LoginOrgForm();
			if (org.getId() == null) {
				log.error("createOrgs--> 组织id为空");
				throw new UserInvalidException("登陆失败！");
			}
			loginOrgForm.setOrgaId(Integer.valueOf(org.getId()));
			loginOrgForm.setOrgaName(org.getName());
			list.add(loginOrgForm);
		});
    	return list;
	}


}
