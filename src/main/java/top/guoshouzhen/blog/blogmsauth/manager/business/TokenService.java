package top.guoshouzhen.blog.blogmsauth.manager.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.guoshouzhen.blog.blogmsauth.config.properties.RsaKeyProperties;
import top.guoshouzhen.blog.blogmsauth.exception.ServiceException;
import top.guoshouzhen.blog.blogmsauth.model.bo.jwt.PlayloadBo;
import top.guoshouzhen.blog.blogmsauth.model.bo.user.UserTokenDetailsBo;
import top.guoshouzhen.blog.blogmsauth.model.constants.TokenConstant;
import top.guoshouzhen.blog.blogmsauth.model.enums.ErrorCodeEnum;
import top.guoshouzhen.blog.blogmsauth.model.po.user.User;
import top.guoshouzhen.blog.blogmsauth.model.vo.AuthVo;
import top.guoshouzhen.blog.blogmsauth.model.vo.TokenVo;
import top.guoshouzhen.blog.blogmsauth.service.user.impl.UserServiceImpl;
import top.guoshouzhen.blog.blogmsauth.utils.JacksonUtil;
import top.guoshouzhen.blog.blogmsauth.utils.JwtUtil;
import top.guoshouzhen.blog.blogmsauth.utils.RedisUtil;
import top.guoshouzhen.blog.blogmsauth.utils.StringUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description userdetailsservice的实现类，需要按自己的业务逻辑去实现loadUserByUsername方法
 * @date 2021/12/27 10:01
 */
@Service
@Slf4j
public class TokenService {
    /**
     * 登陆用户白名单key
     */
    private static final String REDIS_BLOG_USER_LOGIN_WHITELIST = "REDIS_BLOG_USER_LOGIN_WHITELIST";
    /**
     * token的对应key前缀
     */
    private static final String REDIS_BLOG_USER_TOKEN_KEY_PREFIX = "BLOG_USER_";

    @Resource
    private UserServiceImpl userService;
    @Resource
    private RsaKeyProperties rsaKeyProperties;

    /**
     * 根据用户信息生成token
     *
     * @param strDbId 数据库id
     * @param lUserId 用户id
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.TokenVo
     * @author shouzhen.guo
     * @date 2022/5/15 23:10
     */
    public TokenVo getTokenForUser(String strDbId, long lUserId) {
        //验证下传入的用户id是否存在
        User user = userService.selectByPrimaryKey(lUserId, strDbId);
        if (user == null) {
            throw new ServiceException(ErrorCodeEnum.B0001);
        }
        String token = null;
        //先查redis缓存白名单，有直接返回
        Object tokenObj = RedisUtil.hmGet(REDIS_BLOG_USER_LOGIN_WHITELIST, REDIS_BLOG_USER_TOKEN_KEY_PREFIX + user.getId());
        if (tokenObj != null) {
            token = tokenObj.toString();
        } else {
            UserTokenDetailsBo userDetailsBo = new UserTokenDetailsBo(user, strDbId);
            //生成token，并保存到redis白名单，返回token
            token = JwtUtil.generateTokenExpireInSeconds(userDetailsBo, rsaKeyProperties.getRsaPrivateKey(), TokenConstant.TOKEN_VALID_TIME);
            token = TokenConstant.JWT_TOKEN_PREFIX + token;

            //放入redis白名单，字典保存
            //TODO 待优化：过期时间设置不合理
            RedisUtil.hmSet(REDIS_BLOG_USER_LOGIN_WHITELIST, REDIS_BLOG_USER_TOKEN_KEY_PREFIX + user.getId(), token, TokenConstant.TOKEN_VALID_TIME + 10);
        }

        return TokenVo.builder()
                .token(token)
                .build();
    }

    /**
     * 根据用户ID批量删除redis白名单中对应用户的token
     *
     * @param userIds 用户ID
     * @return boolean 删除结果，成功或失败
     * @author shouzhen.guo
     * @date 2022/3/10 16:38
     */
    public boolean removeToken(Long... userIds) {
        if (userIds == null || userIds.length == 0) {
            return true;
        }
        try {
            RedisUtil.hmDel(REDIS_BLOG_USER_LOGIN_WHITELIST, Arrays.stream(userIds)
                    .map(x -> REDIS_BLOG_USER_TOKEN_KEY_PREFIX + x).toArray());
            return true;
        } catch (Exception ex) {
            log.error("从Redis白名单中批量删除用户token时异常，异常信息：{}，用户ID：{}。", ex, JacksonUtil.obj2Json(userIds));
            return false;
        }
    }

    /**
     * 检查token是否有效，验证并返回token中的用户信息
     *
     * @param token token
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.AuthVo.LoginInfoVo
     * @author shouzhen.guo
     * @date 2022/5/17 1:02
     */
    public AuthVo.LoginInfoVo checkToken(String token) {
        //1.验证token是否合法
        if (StringUtil.isNullOrWhiteSpace(token) || !token.startsWith(TokenConstant.JWT_TOKEN_PREFIX)) {
            throw new ServiceException(ErrorCodeEnum.A0005);
        }
        token = token.replace(TokenConstant.JWT_TOKEN_PREFIX, "");
        //2.验证token是否被篡改
        PlayloadBo<UserTokenDetailsBo> playloadBo = null;
        try{
            playloadBo = JwtUtil.getUserInfoFromToken(token, rsaKeyProperties.getRsaPublicKey(), UserTokenDetailsBo.class);
        }catch (Exception ex){
            log.error("Token解析异常，可能被篡改，token：{}，异常信息：{}", token, ex.getMessage());
        }

        if (playloadBo == null || playloadBo.getUserInfo() == null) {
            throw new ServiceException(ErrorCodeEnum.A0005);
        }
        UserTokenDetailsBo userTokenDetailsBo = playloadBo.getUserInfo();
        //3.验证token是否过期，过期的话需要在白名单中删除
        Date dateNow = new Date();
        Date expirDate = playloadBo.getExpiration();
        if (expirDate.compareTo(dateNow) < 0) {
            removeToken(userTokenDetailsBo.getId());
            throw new ServiceException(ErrorCodeEnum.A0006);
        }
        //4.验证token是否在redis白名单中
        Object tokenObj = RedisUtil.hmGet(REDIS_BLOG_USER_LOGIN_WHITELIST, REDIS_BLOG_USER_TOKEN_KEY_PREFIX + userTokenDetailsBo.getId());
        if(tokenObj == null){
            throw new ServiceException(ErrorCodeEnum.A0005);
        }
        AuthVo.LoginInfoVo loginInfoVo = new AuthVo.LoginInfoVo();
        loginInfoVo.setDbId(userTokenDetailsBo.getStrDbId());
        loginInfoVo.setUserId(userTokenDetailsBo.getId());
        return loginInfoVo;
    }
}
