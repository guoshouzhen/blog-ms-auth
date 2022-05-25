package top.guoshouzhen.blog.blogmsauth.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.guoshouzhen.blog.blogmsauth.utils.RsaUtil;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description RSA配置类
 * @date 2021/12/24 17:07
 */
@Data
@Component
public class RsaKeyProperties {
    @Value("${rsa.key.pubKeyPath}")
    private String pubKeyPath;
    @Value("${rsa.key.priKeyPath}")
    private String priKeyPath;

    private PublicKey rsaPublicKey;
    private PrivateKey rsaPrivateKey;

    @PostConstruct
    public void init() throws Exception {
        this.rsaPublicKey = RsaUtil.getPublicKey(pubKeyPath);
        this.rsaPrivateKey = RsaUtil.getPrivateKey(priKeyPath);
    }

}
