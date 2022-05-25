package top.guoshouzhen.blog.blogmsauth.controller.innerapi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.guoshouzhen.blog.blogmsauth.manager.business.AuthService;
import top.guoshouzhen.blog.blogmsauth.manager.business.TokenService;
import top.guoshouzhen.blog.blogmsauth.model.dto.CheckAuthDto;
import top.guoshouzhen.blog.blogmsauth.model.dto.LoginInfoDto;
import top.guoshouzhen.blog.blogmsauth.model.dto.RemoveTokenDto;
import top.guoshouzhen.blog.blogmsauth.model.vo.AuthVo;
import top.guoshouzhen.blog.blogmsauth.model.vo.Result;
import top.guoshouzhen.blog.blogmsauth.model.vo.TokenVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 认证鉴权控制器
 * @date 2022/3/10 18:09
 */
@RestController
@RequestMapping("authcenter/api/innerapi")
public class AuthController {
    @Resource
    private TokenService tokenService;
    @Resource
    private AuthService authService;


    /**
     * 根据用户的信息生成token，并返回token，redis维护token白名单
     *
     * @param loginInfoDto 接口入参定义
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.Result
     * @author shouzhen.guo
     * @date 2022/5/15 23:06
     */
    @PostMapping("/token_get")
    public Result getToken(@RequestBody @Valid LoginInfoDto loginInfoDto) {
        TokenVo tokenVo = tokenService.getTokenForUser(loginInfoDto.getDbId(), loginInfoDto.getUserId());
        return Result.success(tokenVo);
    }

    /**
     * 失效token接口，用户注销按钮或者强制下线时调用
     *
     * @param removeTokenDto 入参
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.Result<java.lang.Object>
     * @author shouzhen.guo
     * @date 2022/3/9 17:53
     */
    @PostMapping("/token_remove")
    public Result removeToken(@RequestBody @Valid RemoveTokenDto removeTokenDto) {
        Long[] aryUserIds = removeTokenDto.getUserIds();
        if (tokenService.removeToken(aryUserIds)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 鉴权接口，验证token有效性，并检查token持有者是否有指定权限
     *
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.Result
     * @author shouzhen.guo
     * @date 2022/5/16 23:37
     */
    @PostMapping("/check_auth")
    public Result checkToken(@RequestBody @Valid CheckAuthDto checkAuthDto) {
        AuthVo.LoginInfoVo loginInfoVo = tokenService.checkToken(checkAuthDto.getToken());
        if (loginInfoVo != null) {
            List<AuthVo.AuthorityVo> authorityVos = authService.checkAuth(loginInfoVo.getDbId(), loginInfoVo.getUserId(), checkAuthDto.getAuths());
            if (authorityVos != null) {
                AuthVo authVo = AuthVo.builder()
                        .loginInfo(loginInfoVo)
                        .authorities(authorityVos)
                        .build();
                return Result.success(authVo);
            }
        }
        return Result.fail();
    }
}
