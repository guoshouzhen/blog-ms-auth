package top.guoshouzhen.blog.blogmsauth.model.bo.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description jwt载荷信息，从token中提取
 * @date 2022/3/2 19:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayloadBo<T> {
    /**
     * token id
     */
    private String id;

    /**
     * 用户信息（可以暴露的）
     */
    private T userInfo;

    /**
     * token过期时间
     */
    private Date expiration;
}
