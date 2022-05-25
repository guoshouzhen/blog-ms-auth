package top.guoshouzhen.blog.blogmsauth.model.po.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    /**
    * 主键id
    */
    private Integer id;

    /**
    * 角色名（权限名）
    */
    private String name;

    /**
    * 角色描述
    */
    private String description;

    /**
    * 创建时间
    */
    private LocalDateTime createDate;

    /**
    * 修改时间
    */
    private LocalDateTime modifyDate;

    /**
    * 状态 0有效 1无效
    */
    private Integer status;
}