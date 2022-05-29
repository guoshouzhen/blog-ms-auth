package top.guoshouzhen.blog.blogmsauth.model.bo.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 实现了GrantedAuthority接口的用户角色权限类
 * @date 2022/1/20 16:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class RoleAuthorityBo {
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
     * 获取角色权限
     * @return 权限名
     */
    @JsonIgnore
    public String getAuthority() {
        return this.name;
    }
}
