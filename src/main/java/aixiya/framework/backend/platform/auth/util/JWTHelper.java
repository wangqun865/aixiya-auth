package aixiya.framework.backend.platform.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

public class JWTHelper {
    private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();

    /**
     * 密钥加密token
     *
     * @param jwtInfo
     * @param priKey
     * @param expire
     * @return
     * @throws Exception
     */
    public static String generateToken(IJWTInfo jwtInfo, byte priKey[], int expire) throws Exception {
        String compactJws = Jwts.builder()
                .setSubject(jwtInfo.getUniqueName())
                .claim(JwtConstants.JWT_KEY_LOGIN_NAME, jwtInfo.getLoginName())
                .claim(JwtConstants.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(JwtConstants.JWT_KEY_NAME, jwtInfo.getName())
                .claim(JwtConstants.JWT_KEY_ORG_ID, jwtInfo.getOrgId())
                .claim(JwtConstants.JWT_KEY_ORG_NAME, jwtInfo.getOrgName())
                .claim(JwtConstants.JWT_KEY_MANAGER, jwtInfo.getManager())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKey))
                .compact();
        return compactJws;
    }


    /**
     * 密钥加密token 无限
     *
     * @param jwtInfo
     * @param priKey
     * @return
     * @throws Exception
     */
    public static String generateToken(IJWTInfo jwtInfo, byte priKey[]) throws Exception {
        String compactJws = Jwts.builder()
                .setSubject(jwtInfo.getUniqueName())
                .claim(JwtConstants.JWT_KEY_LOGIN_NAME, jwtInfo.getLoginName())
                .claim(JwtConstants.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(JwtConstants.JWT_KEY_NAME, jwtInfo.getName())
                .claim(JwtConstants.JWT_KEY_ORG_ID, jwtInfo.getOrgId())
                .claim(JwtConstants.JWT_KEY_ORG_NAME, jwtInfo.getOrgName())
                .claim(JwtConstants.JWT_KEY_MANAGER, jwtInfo.getManager())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKey))
                .compact();
        return compactJws;
    }



    /**
     * 获取token中的用户信息
     *
     * @param token
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static IJWTInfo getInfoFromToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKey);
        Claims body = claimsJws.getBody();
        return new JWTInfo(body.getSubject(),
        		StringHelper.getObjectValue(body.get(JwtConstants.JWT_KEY_LOGIN_NAME)),
        		StringHelper.getObjectValue(body.get(JwtConstants.JWT_KEY_USER_ID)),
                StringHelper.getObjectValue(body.get(JwtConstants.JWT_KEY_NAME)),
                StringHelper.getObjectValue(body.get(JwtConstants.JWT_KEY_ORG_ID)),
                StringHelper.getObjectValue(body.get(JwtConstants.JWT_KEY_ORG_NAME)),
                StringHelper.getObjectValue(body.get(JwtConstants.JWT_KEY_MANAGER)));
    }


    /**
     * 公钥解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKey)).parseClaimsJws(token);
        return claimsJws;
    }

}
