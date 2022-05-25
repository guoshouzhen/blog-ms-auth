package top.guoshouzhen.blog.blogmsauth.model.po.user;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
    * 主键id
    */
    private Long id;

    /**
    * 用户名（昵称）
    */
    private String username;

    /**
    * 真实姓名
    */
    private String fullname;

    /**
    * 用户邮箱
    */
    private String email;

    /**
    * 用户手机
    */
    private String mobile;

    /**
    * 用户密码
    */
    private String password;

    /**
    * 个性签名
    */
    private String signature;

    /**
    * 用户头像
    */
    private String avatar;

    /**
    * 创建时间
    */
    private LocalDateTime createDate;

    /**
    * 修改时间
    */
    private LocalDateTime modifyDate;

    /**
    * 状态 0有效 1无效
    */
    private Integer status;
}