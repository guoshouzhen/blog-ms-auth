package top.guoshouzhen.blog.blogmsauth.mapper.user;

import top.guoshouzhen.blog.blogmsauth.model.annotations.DatabaseSource;
import top.guoshouzhen.blog.blogmsauth.model.enums.DataSourceEnum;
import top.guoshouzhen.blog.blogmsauth.model.po.user.Role;

public interface RoleMapper {
    /**
     * 根据权限id获取权限信息
     * @author shouzhen.guo
     * @date 2022/5/16 0:07
     * @param id 权限id
     * @return top.guoshouzhen.blog.blogmsauth.model.po.user.Role
     */
    @DatabaseSource(DataSourceEnum.MASTER)
    Role selectByPrimaryKey(Integer id);
}