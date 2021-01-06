package aixiya.framework.backend.platform.auth.controller;

import aixiya.framework.backend.platform.auth.common.RedisConstants;
import com.aixiya.framework.backend.common.api.AixiyaFwResponse;
import com.aixiya.framework.backend.common.exception.AixiyaFwException;
import com.aixiya.framework.backend.common.utils.AixiyaFwUtil;
import com.aixiya.framework.backend.redis.starter.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SignOutController {

    private final ConsumerTokenServices consumerTokenServices;
    private final RedisService redisService;

    @GetMapping("signout")
    public AixiyaFwResponse signout(HttpServletRequest request) throws AixiyaFwException {

        String token = AixiyaFwUtil.getCurrentTokenValue();
        AixiyaFwResponse aixiyaFwResponse = new AixiyaFwResponse();

        String jwt = (String)redisService.get(RedisConstants.CLIENT_TOKEN + token);
        //根据jwt中的值删除oauth2 token
        Map<Object, Object>  tokenMap = (LinkedHashMap)redisService.hmget(RedisConstants.JWT + jwt);
        for (Object key : tokenMap.keySet()) {
           consumerTokenServices.revokeToken((String)key);
            //删除自定义的token
            redisService.del((String)RedisConstants.CLIENT_TOKEN + key);
        }
        redisService.del(RedisConstants.JWT + jwt);

        return aixiyaFwResponse.data("退出登录成功");
    }
}
