package top.guoshouzhen.blog.blogmsauth.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/5/16 23:39
 */
@Data
public class CheckAuthDto {
    /**
     * 用户token
     */
    @NotEmpty(message = "token不能为空")
    private String token;
    /**
     * 需要验证的权限列表，可空，不传则默认无
     */
    private String[] auths;
}
