package aixiya.framework.backend.platform.auth.configure;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author wangqun865@163.com
 */
@Configuration
@Data
public class SSOConfiguration {
    @Value("${sso.code.expire}")
    private String codeExpire;

    @Value("${sso.jwt.expire}")
    private String jwtExpire;

    @Value("${sso.jwt.renewal}")
    private String renewal;

    @Value("${sso.jwt.refreshTokenExpire}")
    private String refreshTokenExpire;
}
