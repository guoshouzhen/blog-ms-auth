package top.guoshouzhen.blog.blogmsauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {
        /**
         * 排除数据源自动装配
         */
        DataSourceAutoConfiguration.class
})
@EnableCaching
public class BlogMsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogMsAuthApplication.class, args);
    }

}
