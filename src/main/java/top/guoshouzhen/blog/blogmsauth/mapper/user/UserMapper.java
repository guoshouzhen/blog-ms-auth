package top.guoshouzhen.blog.blogmsauth.mapper.user;

import top.guoshouzhen.blog.blogmsauth.model.po.user.User;

public interface UserMapper {
    User selectByPrimaryKey(Long id);
}