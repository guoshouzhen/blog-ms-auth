package top.guoshouzhen.blog.blogmsauth.service.user.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.guoshouzhen.blog.blogmsauth.mapper.user.RoleMapper;
import top.guoshouzhen.blog.blogmsauth.model.po.user.Role;
import top.guoshouzhen.blog.blogmsauth.service.user.RoleService;

import javax.annotation.Resource;

@Service
public class RoleServiceImpl implements RoleService{

    @Resource
    private RoleMapper roleMapper;

    /**
     * 根据角色id查询角色信息（缓存）
     * @author shouzhen.guo
     * @date 2022/5/16 0:31
     * @param id 角色id
     * @return top.guoshouzhen.blog.blogmsauth.model.po.user.Role
     */
    @Override
    @Cacheable(cacheNames = "RoleInfo", key = "'role_'.concat(#id)", unless = "#result == null ")
    public Role selectByPrimaryKey(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }
}
