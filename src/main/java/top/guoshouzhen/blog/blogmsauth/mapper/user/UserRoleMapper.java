package top.guoshouzhen.blog.blogmsauth.mapper.user;

import top.guoshouzhen.blog.blogmsauth.model.po.user.UserRole;

import java.util.List;

public interface UserRoleMapper {
    List<UserRole> selectUserRoleByUserId(Long lUserId);
}