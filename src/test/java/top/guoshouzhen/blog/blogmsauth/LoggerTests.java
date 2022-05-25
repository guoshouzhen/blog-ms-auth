package top.guoshouzhen.blog.blogmsauth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.guoshouzhen.blog.blogmsauth.utils.RedisUtil;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 日志相关测试
 * @date 2022/3/1 19:57
 */
@SpringBootTest
public class LoggerTests {
    @Test
    public void testUniqueId(){
        String table1 = "user";
        String table2 = "blog";
        for(int i = 0; i < 100; i++){
            long userId = RedisUtil.getIncrementId(table1);
            long blogId = RedisUtil.getIncrementId(table2);

            System.out.println("user Id：" + userId);
            System.out.println("blog Id：" + blogId);
        }
    }
}
