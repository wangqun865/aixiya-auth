package aixiya.framework.backend.platform.auth.configure;


import aixiya.framework.backend.platform.auth.util.RsaKeyHelper;
import com.aixiya.framework.backend.redis.starter.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
* @Description:  秘钥再加密，存入redis （公钥私钥暂时为同一个）  暂时只用user 以后扩展需要
* @Author:wangqun865@163.com
*/
@Configuration
public class SsoRedisRunner implements CommandLineRunner {

    @Autowired
    private RedisService redisService;

    private static final String REDIS_USER_PRI_KEY = "AIXIYA:SSO:JWT:PRI";
    private static final String REDIS_USER_PUB_KEY = "AIXIYA:SSO:JWT:PUB";
    private static final String REDIS_SERVICE_PRI_KEY = "AIXIYA:SSO:CLIENT:PRI";
    private static final String REDIS_SERVICE_PUB_KEY = "AIXIYA:SSO:CLIENT:PUB";

    @Autowired
    private KeyConfiguration keyConfiguration;

    @Override
    public void run(String... args) throws Exception {
        if(redisService.hasKey(REDIS_USER_PRI_KEY) && redisService.hasKey(REDIS_USER_PUB_KEY)
                &&redisService.hasKey(REDIS_SERVICE_PRI_KEY) && redisService.hasKey(REDIS_SERVICE_PUB_KEY)) {
            keyConfiguration.setUserPriKey(RsaKeyHelper.toBytes(redisService.get(REDIS_USER_PRI_KEY).toString()));
            keyConfiguration.setUserPubKey(RsaKeyHelper.toBytes(redisService.get(REDIS_USER_PUB_KEY).toString()));
            keyConfiguration.setServicePriKey(RsaKeyHelper.toBytes(redisService.get(REDIS_SERVICE_PRI_KEY).toString()));
            keyConfiguration.setServicePubKey(RsaKeyHelper.toBytes(redisService.get(REDIS_SERVICE_PUB_KEY).toString()));
        } else {
            Map<String, byte[]> keyMap = RsaKeyHelper.generateKey(keyConfiguration.getUserSecret());
            keyConfiguration.setUserPriKey(keyMap.get("pri"));
            keyConfiguration.setUserPubKey(keyMap.get("pub"));
            redisService.set(REDIS_USER_PRI_KEY, RsaKeyHelper.toHexString(keyMap.get("pri")));
            redisService.set(REDIS_USER_PUB_KEY, RsaKeyHelper.toHexString(keyMap.get("pub")));
            keyMap = RsaKeyHelper.generateKey(keyConfiguration.getServiceSecret());
            keyConfiguration.setServicePriKey(keyMap.get("pri"));
            keyConfiguration.setServicePubKey(keyMap.get("pub"));
            redisService.set(REDIS_SERVICE_PRI_KEY, RsaKeyHelper.toHexString(keyMap.get("pri")));
            redisService.set(REDIS_SERVICE_PUB_KEY, RsaKeyHelper.toHexString(keyMap.get("pub")));
            //pri :[B@e2d56bf
            //pub :[B@244038d0
        }


    }
}
