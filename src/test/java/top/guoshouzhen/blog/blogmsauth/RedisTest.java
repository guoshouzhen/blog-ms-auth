package top.guoshouzhen.blog.blogmsauth;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.guoshouzhen.blog.blogmsauth.utils.RedisUtil;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/5/15 13:19
 */
@SpringBootTest
public class RedisTest {
    @Test
    public void testRedis(){
        RedisUtil.set("redisTest", "2333");

        System.out.println(RedisUtil.get("redisTest"));
    }
}
