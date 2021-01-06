package aixiya.framework.backend.platform.auth;

import com.aixiya.framework.backend.security.starter.annotation.EnablePlatformCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wangqun865@163.com
 */
@SpringBootApplication
@EnablePlatformCloudResourceServer
@MapperScan("aixiya.framework.backend.platform.auth.mapper")
@EnableFeignClients({"aixiya.framework.backend.platform.foundation.api" , "aixiya.framework.backend.platform.user.api"})
@EnableCircuitBreaker
@EnableHystrix
@EnableDiscoveryClient
public class AixiyaAuthCenterApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AixiyaAuthCenterApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
