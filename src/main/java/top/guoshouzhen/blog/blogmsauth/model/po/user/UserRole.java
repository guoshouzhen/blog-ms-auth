package top.guoshouzhen.blog.blogmsauth.model.po.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    /**
    * 主键id
    */
    private Long id;

    /**
    * 用户id
    */
    private Long userId;

    /**
    * 角色id
    */
    private Integer roleId;

    /**
    * 状态（0有效 1无效）
    */
    private Integer status;
}