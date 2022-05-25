package top.guoshouzhen.blog.blogmsauth;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.guoshouzhen.blog.blogmsauth.utils.RsaUtil;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/5/14 23:21
 */
@SpringBootTest
public class RsaTest {
    private final String rsaPubKeyPath = "D:\\blog\\fileDoc\\authcenter\\rsaKey\\key_rsa.pub";
    private final String rsaPriKeyPath = "D:\\blog\\fileDoc\\authcenter\\rsaKey\\key_rsa";

    @Test
    public void testCreateRsaKey(){
        try{
            RsaUtil.generateKey(rsaPubKeyPath, rsaPriKeyPath);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
