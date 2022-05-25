package top.guoshouzhen.blog.blogmsauth.manager.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.guoshouzhen.blog.blogmsauth.model.po.user.Role;
import top.guoshouzhen.blog.blogmsauth.model.vo.AuthVo;
import top.guoshouzhen.blog.blogmsauth.service.user.UserRoleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 权限处理服务
 * @date 2022/5/17 0:51
 */
@Service
@Slf4j
public class AuthService {

    @Resource
    private UserRoleService userRoleService;

    /**
     * 检查token，并检查指定权限
     *
     * @param strDbId      数据库id
     * @param lUserId      用户id
     * @param aryAuthNames 验证的权限列表
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.AuthVo
     * @author shouzhen.guo
     * @date 2022/5/17 1:42
     */
    public List<AuthVo.AuthorityVo> checkAuth(String strDbId, long lUserId, String[] aryAuthNames) {
        List<AuthVo.AuthorityVo> lstResults = new ArrayList<>();
        if(aryAuthNames == null || aryAuthNames.length == 0){
            return lstResults;
        }
        //获取用户的所有权限
        List<Role> lstRoles = userRoleService.getUserRolesByUserId(strDbId, lUserId);
        Set<String> authorizedNameSet = null;
        if (lstRoles != null) {
            authorizedNameSet = lstRoles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
        }
        for (String authName : aryAuthNames) {
            AuthVo.AuthorityVo authorityVo = new AuthVo.AuthorityVo();
            authorityVo.setAuthName(authName);
            authorityVo.setAuthorized(authorizedNameSet != null && authorizedNameSet.contains(authName));
            lstResults.add(authorityVo);
        }
        return lstResults;
    }
}
