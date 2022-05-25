package top.guoshouzhen.blog.blogmsauth.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/5/17 0:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthVo {
    /**
     * 登录用户信息
     */
    private LoginInfoVo loginInfo;
    /**
     * 用户权限信息
     */
    private List<AuthorityVo> authorities;

    @Data
    public static class LoginInfoVo{
        /**
         * 用户id
         */
        private long userId;
        /**
         * 数据库id
         */
        private String dbId;
    }

    @Data
    public static class AuthorityVo{
        /**
         * 权限名
         */
        private String authName;
        /**
         * 是否有该权限
         * 坑：jackson序列化时会自动去掉字段的is前缀
         * 指定属性名
         */
        @JsonProperty("isAuthorized")
        private boolean isAuthorized;
    }
}
