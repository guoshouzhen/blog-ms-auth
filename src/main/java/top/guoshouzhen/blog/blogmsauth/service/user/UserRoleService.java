package top.guoshouzhen.blog.blogmsauth.service.user;

import top.guoshouzhen.blog.blogmsauth.model.po.user.Role;

import java.util.List;

public interface UserRoleService{

    List<Role> getUserRolesByUserId(String strDbId, Long lUserId);

}
