package aixiya.framework.backend.platform.auth.common.util;

import aixiya.framework.backend.platform.auth.configure.KeyConfiguration;
import aixiya.framework.backend.platform.auth.util.IJWTInfo;
import aixiya.framework.backend.platform.auth.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
* @Author:wangqun865@163.com
*/
@Component
public class JwtTokenUtil {

    @Value("${jwt.expire}")
    private int expire;
    @Autowired
    private KeyConfiguration keyConfiguration;


    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo, keyConfiguration.getUserPriKey() ,expire);
    }

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        return JWTHelper.getInfoFromToken(token, keyConfiguration.getUserPubKey());
    }


}
