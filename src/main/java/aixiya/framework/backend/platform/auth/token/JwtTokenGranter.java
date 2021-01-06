package aixiya.framework.backend.platform.auth.token;

import aixiya.framework.backend.platform.auth.common.RedisConstants;
import aixiya.framework.backend.platform.auth.common.util.JwtTokenUtil;
import aixiya.framework.backend.platform.auth.controller.form.AuthenticateJson;
import aixiya.framework.backend.platform.auth.exception.ClientValidException;
import aixiya.framework.backend.platform.auth.util.IJWTInfo;
import com.aixiya.framework.backend.redis.starter.service.RedisService;
import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class JwtTokenGranter  extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "jwt";
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private RedisService redisService;

    public JwtTokenGranter(AuthorizationServerTokenServices tokenServices,
                           ClientDetailsService clientDetailsService,
                           OAuth2RequestFactory requestFactory,
                           final AuthenticationManager authenticationManager,
                           JwtTokenUtil jwtTokenUtil,
                           RedisService redisService) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisService = redisService;
    }


    protected JwtTokenGranter(
            AuthenticationManager authenticationManager,
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            String grantType,
            JwtTokenUtil jwtTokenUtil ,
            RedisService redisService) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisService =  redisService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client,
                                                           TokenRequest tokenRequest) {


        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        //HttpServletRequest request = AixiyaFwUtil.getHttpServletRequest();

        //String code = request.getHeader("jwt");
        String code = parameters.get("aixiya_jwt");
        IJWTInfo info ;
        String username = "";
        String password = "";
        String clientId = parameters.get("aixiya_client_id");
        //String clientId = request.getHeader("client_id");
        try {
            String codeString = (String)redisService.get(RedisConstants.CODE + code);
            logger.error("JwtTokenGranter-->RedisConstants.CODE + code-->" + code);
            if(codeString == null || codeString.length() == 0) {
                logger.error("JwtTokenGranter-->getOAuth2Authentication--> no code");
                throw new ClientValidException("产品无效");
            }
            byte[] base64decodedBytes = Base64.getDecoder().decode(codeString);
            String authenticateJsonStr = new String(base64decodedBytes);
            logger.error("JwtTokenGranter-->authenticateJsonStr-->" + authenticateJsonStr);
            AuthenticateJson authenticateJson = JSON.parseObject(authenticateJsonStr, AuthenticateJson.class);
            username = authenticateJson.getLoginName() + "wq/@#&,cfwq" + authenticateJson.getOrgId() + "wq/@#&,cfwq" + authenticateJson.getClientId()
                    + "wq/@#&,cfwq" + authenticateJson.getManager();
            //password = codeString;
            password = "test";
            logger.error("JwtTokenGranter-->clientId-->" + clientId);
            if(!clientId.equals(authenticateJson.getClientId())) {
                logger.error("JwtTokenGranter-->getOAuth2Authentication-->: clientID not Equals" + clientId + "----" + authenticateJson.getClientId());
                throw new ClientValidException("产品无效");
            }
        } catch (Exception e) {
            logger.error("JwtTokenGranter-->getOAuth2Authentication-->" + e.getMessage());
            logger.error(e.getMessage(), e);
            throw new ClientValidException("产品无效");
        }

        //从jwt中获取用户名
        parameters.remove("aixiya_jwt");
        parameters.remove("aixiya_client_id");
        Authentication userAuth = new JwtAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        try {
            userAuth = authenticationManager.authenticate(userAuth);//调用上面的provide 验证
        } catch (AccountStatusException ase) {
//covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        } catch (BadCredentialsException e) {
// If the username/password are wrong the spec says we should send 400/invalid grant
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}