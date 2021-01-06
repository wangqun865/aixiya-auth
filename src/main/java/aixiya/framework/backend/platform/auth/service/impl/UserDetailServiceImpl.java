package aixiya.framework.backend.platform.auth.service.impl;

import aixiya.framework.backend.platform.auth.common.util.JwtTokenUtil;
import aixiya.framework.backend.platform.auth.configure.UserAuthUtil;
import aixiya.framework.backend.platform.user.api.UserAuthClient;
import aixiya.framework.backend.platform.user.api.entity.User;
import com.aixiya.framework.backend.common.entity.AixiyaFwAuthUser;
import com.aixiya.framework.backend.redis.starter.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangqun865@163.com
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    //private final UserManager userManager;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserAuthUtil userAuthUtil;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserAuthClient userAuthClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

////        HttpSession session = httpServletRequest.getSession(false);
////        Object attribute = session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        //HttpServletRequest httpServletRequest = AixiyaFwUtil.getHttpServletRequest();
        //String code = httpServletRequest.getHeader("jwt");

        try {
          //  IJWTInfo j = userAuthUtil.getInfoFromToken(jwt);
          //  IJWTInfo i =  jwtTokenUtil.getInfoFromToken(jwt);
        } catch (Exception e) {
          //  log.error("loadUserByUsername error" + e.getMessage());
          //  throw new UsernameNotFoundException("");
        }

        // todo校验code是否合法
        // add by 0918 JwtTokenGranter 已经校验过code是否合法  可去掉此处校验
       /* String codeString = (String)redisService.get(RedisConstants.CODE + code);
        if (codeString == null || codeString.length() == 0) {
            log.error("loadUserByUsername--> loadUserByUsername fail!  code: " + code);
            throw new UsernameNotFoundException("loadUserByUsername fail!");
        }
        AuthenticateJson authenticateJson = null;
        try {
            byte[] base64decodedBytes = Base64.getDecoder().decode(codeString);
            String authenticateJsonStr = new String(base64decodedBytes);
            authenticateJson = JSON.parseObject(authenticateJsonStr, AuthenticateJson.class);
        } catch (Exception e) {
            log.error("loadUserByUsername--> parseObject fail!  codeString: " + codeString);
            throw new UsernameNotFoundException("parse user fail!");
        }*/
        //new add


        // add by0918  调用API 获取系统用户
        //todo 1/去掉熔断  2/可考虑增加熔断的线程处理  3/使用json处理
        // todo yours business
        String[] users = username.split("wq/@#&,cfwq");
        username = users[0];
        String orgId = users[1];
        String clientId = users[2];
        String manager = users[3];
        User user = userAuthClient.getUserByLoginName(username,orgId);
        if (user != null) {
            String permissions = "";
            //todo 这里要用户中心改成不需要产品
            String[] permissionStrings = userAuthClient.getResourceCode(username,
                    Long.valueOf(orgId),clientId);
            for (int i = 0 ; i < permissionStrings.length ; i++) {
                if (i != (permissionStrings.length-1)) {
                    permissions = permissions + permissionStrings[i] + ",";
                } else {
                    permissions = permissions + permissionStrings[i];
                }
            }
            // todo 暂时无需校验，用户中心可用字段是byte？？ 到这给的都是可用的，不可用应该就不返回了
            //真去掉
            /*boolean notLocked = false;
            if (StringUtils.equals(SystemUser.STATUS_VALID, user.getDisabled())) {
                notLocked = true;
            }*/
            String password = user.getLoginPwd();
            //暂时没用
            /*String loginType = (String) httpServletRequest.getAttribute(ParamsConstant.LOGIN_TYPE);
            if (StringUtils.equals(loginType, SocialConstant.SOCIAL_LOGIN)) {
                password = passwordEncoder.encode(SocialConstant.SOCIAL_LOGIN_PASSWORD);
            }*/

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
            if (StringUtils.isNotBlank(permissions)) {
                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
            }
            /*if(StringUtils.isNotBlank(codeString)){
                password = codeString;
            }*/
            password = "test";
            AixiyaFwAuthUser authUser = new AixiyaFwAuthUser(user.getLoginName(), password, true, true, true, false,
                    grantedAuthorities);

            BeanUtils.copyProperties(user, authUser);
            /*authUser.setOrgId(authenticateJson.getOrgId());
            authUser.setOrgName(authenticateJson.getOrgName());*/
            authUser.setOrgId(user.getOrganization().getId());
            authUser.setOrgName(user.getOrganization().getName());
            authUser.setOrgSsopId(user.getOrganization().getSsopId());
            authUser.setManager(manager);
            return authUser;
        } else {
            log.error("loadUserByUsername-->  user is null !  username: " + username);
            throw new UsernameNotFoundException("user is not exist!");
        }







        // todo校验jwt中的用户和prodcut是否匹配
        // add by 0918 修改user 框架逻辑封装
       /*SystemUser systemUser = userManager.findByName(username);
        if (systemUser != null) {
            String permissions = userManager.findUserPermissions(systemUser.getUsername());
            boolean notLocked = false;
            if (StringUtils.equals(SystemUser.STATUS_VALID, systemUser.getStatus())) {
                notLocked = true;
            }
            String password = systemUser.getPassword();
            String loginType = (String) httpServletRequest.getAttribute(ParamsConstant.LOGIN_TYPE);
            if (StringUtils.equals(loginType, SocialConstant.SOCIAL_LOGIN)) {
                password = passwordEncoder.encode(SocialConstant.SOCIAL_LOGIN_PASSWORD);
            }

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
            if (StringUtils.isNotBlank(permissions)) {
                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
            }
            if(StringUtils.isNotBlank(codeString)){
                password = codeString;
            }
            AixiyaFwAuthUser authUser = new AixiyaFwAuthUser(systemUser.getUsername(), password, true, true, true, notLocked,
                    grantedAuthorities);

            BeanUtils.copyProperties(systemUser, authUser);
            return authUser;
        } else {
            log.error("loadUserByUsername-->  user is null !  username: " + username);
            throw new UsernameNotFoundException("user is not exist!");
        }*/
    }

}
