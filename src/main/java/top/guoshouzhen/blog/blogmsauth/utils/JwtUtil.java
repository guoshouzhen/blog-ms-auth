package top.guoshouzhen.blog.blogmsauth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import top.guoshouzhen.blog.blogmsauth.model.bo.jwt.PlayloadBo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description token的生成及校验相关
 * @date 2021/12/23 17:46
 */
public class JwtUtil {
    private static final String JWT_PAYLOAD_USER_KEY = "user";

    /**
     * 私钥加密token
     * @author guoshouzhen
     * @date 2021/12/23 21:33
     * @param userInfo 用户信息
     * @param privateKey 私钥
     * @param expire 有效时间（秒）
     * @return java.lang.String
     */
    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, long expire){
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JacksonUtil.obj2Json(userInfo))
                .setId(createJTI())
                .setExpiration(DateUtil.localDateTime2Date(LocalDateTime.now().plusSeconds(expire)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
    private static String createJTI(){
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     * 公钥解析token
     * @author guoshouzhen
     * @date 2021/12/23 21:39
     * @param token token
     * @param publicKey 公钥
     * @return io.jsonwebtoken.Jws<io.jsonwebtoken.Claims>
     */
    private static Jws<Claims> parseToken(String token, PublicKey publicKey){
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     * @author shouzhen.guo
     * @date 2021/12/24 12:16
     * @param token token
     * @param publicKey 公钥
     * @param clazz class
     * @return top.guoshouzhen.cloud.model.Payload<T>
     */
    public static <T> PlayloadBo<T> getUserInfoFromToken(String token, PublicKey publicKey, Class<T> clazz) throws Exception{
        Jws<Claims> claimsJws = parseToken(token, publicKey);
        Claims body = claimsJws.getBody();
        PlayloadBo<T> playloadBo = new PlayloadBo<>();
        playloadBo.setId(body.getId());
        playloadBo.setExpiration(body.getExpiration());
        playloadBo.setUserInfo(JacksonUtil.json2Obj(body.get(JWT_PAYLOAD_USER_KEY).toString(), clazz));
        return playloadBo;
    }

    /**
     * 获取token中的用户信息
     * @author shouzhen.guo
     * @date 2021/12/24 12:18
     * @param token token
     * @param publicKey 公钥
     * @return top.guoshouzhen.cloud.model.Payload<T>
     */
    public static <T> PlayloadBo<T> getUserInfoFromToken(String token, PublicKey publicKey){
        Jws<Claims> claimsJws = parseToken(token, publicKey);
        Claims body = claimsJws.getBody();
        PlayloadBo<T> playloadBo = new PlayloadBo<>();
        playloadBo.setId(body.getId());
        playloadBo.setExpiration(body.getExpiration());
        return playloadBo;
    }
}
