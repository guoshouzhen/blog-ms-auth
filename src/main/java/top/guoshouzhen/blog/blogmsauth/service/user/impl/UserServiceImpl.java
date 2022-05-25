package top.guoshouzhen.blog.blogmsauth.service.user.impl;

import org.springframework.stereotype.Service;
import top.guoshouzhen.blog.blogmsauth.mapper.user.UserMapper;
import top.guoshouzhen.blog.blogmsauth.model.po.user.User;
import top.guoshouzhen.blog.blogmsauth.service.user.UserService;
import top.guoshouzhen.blog.blogmsauth.utils.DbUtil;

import javax.annotation.Resource;
@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * 从指定数据库查询用户信息
     * @author shouzhen.guo
     * @date 2022/5/15 23:21
     * @param id 用户id
     * @param dbId 数据库id
     * @return top.guoshouzhen.blog.blogmsauth.model.po.user.User
     */
    @Override
    public User selectByPrimaryKey(Long id, String dbId) {
        DbUtil.switchDateSource(dbId);
        User user = userMapper.selectByPrimaryKey(id);
        DbUtil.resetDataSource();
        return user;
    }
}
