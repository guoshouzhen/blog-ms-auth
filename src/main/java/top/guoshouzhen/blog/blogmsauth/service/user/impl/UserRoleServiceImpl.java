package top.guoshouzhen.blog.blogmsauth.service.user.impl;

import org.springframework.stereotype.Service;
import top.guoshouzhen.blog.blogmsauth.mapper.user.UserRoleMapper;
import top.guoshouzhen.blog.blogmsauth.model.po.user.Role;
import top.guoshouzhen.blog.blogmsauth.model.po.user.UserRole;
import top.guoshouzhen.blog.blogmsauth.service.user.UserRoleService;
import top.guoshouzhen.blog.blogmsauth.utils.DbUtil;
import top.guoshouzhen.blog.blogmsauth.utils.StringUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService{

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleServiceImpl roleService;

    /**
     * 查询用户的角色信息
     * @author shouzhen.guo
     * @date 2022/5/16 0:36
     * @param strDbId 数据库id
     * @param lUserId 用户id
     * @return java.util.List<top.guoshouzhen.blog.blogmsauth.model.po.user.UserRole>
     */
    @Override
    public List<Role> getUserRolesByUserId(String strDbId, Long lUserId) {
        if(StringUtil.isNullOrWhiteSpace(strDbId)){
            return null;
        }
        List<Role> lstRoles = null;
        DbUtil.switchDateSource(strDbId);
        List<UserRole> lstUserRoleInfos = userRoleMapper.selectUserRoleByUserId(lUserId);
        DbUtil.resetDataSource();
        if(lstUserRoleInfos != null){
            lstRoles = new ArrayList<>(lstUserRoleInfos.size());
            for (UserRole userRole : lstUserRoleInfos){
                Role role = roleService.selectByPrimaryKey(userRole.getRoleId());
                if(role != null){
                    lstRoles.add(role);
                }
            }
        }
        return lstRoles;
    }
}
