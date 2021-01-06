package aixiya.framework.backend.platform.auth.token;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;

@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
   // private VerificationCodeService verificationCodeService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //这个authentication就是JwtAuthenticationToken
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());

        String dbPassword = user.getPassword();
        String password = authenticationToken.getPassword();
        if (StringUtils.isEmpty(dbPassword) || !password.equals(dbPassword)) {
            log.error("JwtAuthenticationProvider-->authenticate password error ");
            throw new InvalidGrantException("密码错误");
        }
        //这时候已经认证成功了
        JwtAuthenticationToken authenticationResult = new JwtAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }


    public void setUserDetailsService (UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
