package top.guoshouzhen.blog.blogmsauth.model.constants;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description Jwt相关常量信息
 * @date 2021/12/27 15:14
 */
public class TokenConstant {
    /**
     * token的header key
     */
    public static final String TOKEN_HEADER = "Authorization";
    /**
     * Authorization token 前缀
     */
    public static final String JWT_TOKEN_PREFIX = "blog ";

    /**
     * token有效期，一天
     */
    public static final long TOKEN_VALID_TIME = 24 * 60 * 60;
}
