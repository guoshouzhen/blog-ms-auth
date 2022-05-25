package top.guoshouzhen.blog.blogmsauth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description OAuth2.0返回的token信息封装
 * @date 2022/1/20 18:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenVo {
    /**
     * access token，设置有效期，强制失效借助redis实现白名单（key - dict）
     */
    private String token;
}
