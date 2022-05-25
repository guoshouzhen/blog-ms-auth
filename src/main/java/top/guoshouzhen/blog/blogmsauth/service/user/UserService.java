package top.guoshouzhen.blog.blogmsauth.service.user;

import top.guoshouzhen.blog.blogmsauth.model.po.user.User;
public interface UserService{

    User selectByPrimaryKey(Long id, String dbId);

}
