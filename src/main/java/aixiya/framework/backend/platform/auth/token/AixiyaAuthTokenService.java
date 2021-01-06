package aixiya.framework.backend.platform.auth.token;

import aixiya.framework.backend.platform.auth.common.RedisConstants;
import aixiya.framework.backend.platform.auth.configure.SSOConfiguration;
import aixiya.framework.backend.platform.auth.controller.form.AuthenticateJson;
import aixiya.framework.backend.platform.auth.exception.ClientValidException;
import aixiya.framework.backend.platform.auth.exception.ReflushValidException;
import com.aixiya.framework.backend.redis.starter.service.RedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j

public class AixiyaAuthTokenService extends DefaultTokenServices {
    @Autowired
    private SSOConfiguration sSOConfiguration;
    private final RedisService redisService;
    public AixiyaAuthTokenService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        //todo your business
        //HttpServletRequest httpServletRequest = AixiyaFwUtil.getHttpServletRequest();
        Map<String, String> map1 = authentication.getOAuth2Request().getRequestParameters();
        String code = map1.get("aixiya_jwt");
        //String code = httpServletRequest.getHeader("jwt");
        OAuth2AccessToken token = super.createAccessToken(authentication);
        String codeString = (String)redisService.get(RedisConstants.CODE + code);
        if(codeString == null || codeString.length() == 0) {
            log.info("createAccessToken redis no match , code :" + code);
            throw new ClientValidException("产品无效");
        }
        byte[] base64decodedBytes = Base64.getDecoder().decode(codeString);
        String authenticateJsonStr = new String(base64decodedBytes);
        AuthenticateJson authenticateJson = JSON.parseObject(authenticateJsonStr, AuthenticateJson.class);
        String jwt = authenticateJson.getJwt();

        if(authentication.getUserAuthentication().getDetails() != null){
            LinkedHashMap map = (LinkedHashMap)authentication.getUserAuthentication().getDetails();
            String grant_type = (String)map.get("grant_type");
            if(StringUtils.equals("jwt",grant_type)){
                //todo 确认refreshtoken刷新时间 暂定比token多2个小时
                redisService.set(RedisConstants.CLIENT_TOKEN + token.getValue(),jwt,Long.valueOf(sSOConfiguration.getRefreshTokenExpire()));
                redisService.hset(RedisConstants.JWT + jwt,token.getValue(),token.getValue());
                //redisService.lSet(jwt,token.getValue());
            }
        }
        return token;
    }


    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
            throws AuthenticationException {


        OAuth2AccessToken token = super.refreshAccessToken(refreshTokenValue,tokenRequest);
        //更新token和jwt
        String accessToken = tokenRequest.getRequestParameters().get("access_token");
        String jwt = (String)redisService.get(RedisConstants.CLIENT_TOKEN + accessToken);

        Long e = redisService.getExpire(RedisConstants.JWT + jwt);
        if(e < 0) {
            log.error("/authenticate jwt no match :" + jwt);
            throw new ReflushValidException("续期失败");
        }


        //todo 确认refreshtoken刷新时间 暂定比token多2个小时
        redisService.set(RedisConstants.CLIENT_TOKEN + token.getValue(),jwt,(long)token.getExpiresIn()+ 60 *60 *2);
        redisService.del(RedisConstants.CLIENT_TOKEN + accessToken);

        //更新jwt和tokon
        redisService.hset(RedisConstants.JWT + jwt,token.getValue(),token.getValue());
        redisService.hdel(RedisConstants.JWT + jwt,accessToken);



        // 为JWT续期
        Long renewal = Long.valueOf(sSOConfiguration.getRenewal());
        if (e < renewal) {
            boolean flag = redisService.expire(RedisConstants.JWT + jwt ,renewal);
            if (!flag) {
                log.error("/authenticate redis error :" + jwt);
                throw new ReflushValidException("续期失败!");
            }
        }



        return token;
        //续期refresh_token
       /* Long eRefresh = redisService.getExpire("refresh:" + refreshTokenValue);
        if(eRefresh < 0) {
            log.error("/authenticate jwt no match :" + jwt);
            throw new ReflushValidException("续期失败");
        }
        boolean f1 = redisService.expire("refresh:" + refreshTokenValue ,eRefresh + 60 * 100);
        boolean f2 = redisService.expire("refresh_auth:" + refreshTokenValue ,eRefresh + 60 * 100);
        boolean f3 = redisService.expire("refresh_to_access:" + refreshTokenValue ,eRefresh + 60 * 100);
        boolean f4 = redisService.expire("access_to_refresh:" + accessToken ,eRefresh + 60 * 100);

        if (!f1 || !f2 || !f3 || !f4) {
            log.error("/authenticate redis error :" + jwt);
            throw new ReflushValidException("续期失败!");
        }*/



    }
}
