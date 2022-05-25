package top.guoshouzhen.blog.blogmsauth.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description token失效接口参数
 * @date 2022/3/9 17:48
 */
@Data
public class RemoveTokenDto {
    /**
     * 用户ID列表
     */
    @NotNull(message = "用户id不能为空")
    private Long[] userIds;
}
