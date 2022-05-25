package top.guoshouzhen.blog.blogmsauth.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 登陆时需要的参数
 * @date 2022/3/2 19:01
 */
@Data
public class LoginInfoDto {
    /**
     * 数据库id
     */
    @NotEmpty(message = "数据库id不能为空")
    private String dbId;
    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;
}
