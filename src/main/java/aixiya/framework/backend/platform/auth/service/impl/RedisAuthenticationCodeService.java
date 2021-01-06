package aixiya.framework.backend.platform.auth.service.impl;


import com.aixiya.framework.backend.redis.starter.service.RedisService;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;

@Service
public class RedisAuthenticationCodeService extends RandomValueAuthorizationCodeServices {
    private static final String AUTH_CODE_KEY = "auth_code";
    private final RedisService redisService;

    public RedisAuthenticationCodeService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        try {
            OAuth2Authentication authentication = null;

            String value = (String) redisService.hget(AUTH_CODE_KEY, code);
            if (StringUtils.isBlank(value)) {
                return null;
            } else {
                authentication = JSONObject.parseObject(value, OAuth2Authentication.class);
                redisService.hdel(AUTH_CODE_KEY, code);
                return authentication;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        try {
            redisService.hset(AUTH_CODE_KEY, code,
                    JSONObject.toJSONString(authentication));
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
