package aixiya.framework.backend.platform.auth.configure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangqun865@163.com
 */
@Configuration
@ComponentScan(basePackages = { "aixiya.framework.backend.platform.foundation.api" , "aixiya.framework.backend.platform.user.api"})
public class StorageConfiguration {
}
