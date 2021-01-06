package aixiya.framework.backend.platform.auth.configure;


import aixiya.framework.backend.platform.auth.common.exception.UserTokenException;
import aixiya.framework.backend.platform.auth.util.IJWTInfo;
import aixiya.framework.backend.platform.auth.util.JWTHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:wangqun865@163.com
 */
@Configuration
public class UserAuthUtil {
    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private KeyConfiguration keyConfiguration;

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, keyConfiguration.getUserPubKey());

           // return JWTHelper.getInfoFromToken(token, userAuthConfig.getPubKeyByte());
        }catch (ExpiredJwtException ex){
            throw new UserTokenException("User token expired!");
        }catch (SignatureException ex){
            throw new UserTokenException("User token signature error!");
        }catch (IllegalArgumentException ex){
            throw new UserTokenException("User token is null or empty!");
        }
    }
}
