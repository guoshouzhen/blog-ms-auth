package top.guoshouzhen.blog.blogmsauth.model.bo.user;

import lombok.Data;
import top.guoshouzhen.blog.blogmsauth.model.po.user.User;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description UserDetails接口的实现类
 * @date 2022/1/20 16:00
 */
@Data
public class UserTokenDetailsBo {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 数据库ID
     */
    private String strDbId;

    public UserTokenDetailsBo() { }

    /**
     * 构造用户信息
     * @author shouzhen.guo
     * @date 2022/5/16 0:57
     * @param user 用户信息
     * @param strDbId 数据库id
     * @return null
     */
    public UserTokenDetailsBo(User user, String strDbId) {
        this.id = user.getId();
        this.strDbId = strDbId;
    }
}
