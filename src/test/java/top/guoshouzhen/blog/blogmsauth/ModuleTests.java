package top.guoshouzhen.blog.blogmsauth;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.guoshouzhen.blog.blogmsauth.model.po.user.Role;
import top.guoshouzhen.blog.blogmsauth.service.user.RoleService;
import top.guoshouzhen.blog.blogmsauth.service.user.impl.RoleServiceImpl;
import top.guoshouzhen.blog.blogmsauth.utils.RedisUtil;
import top.guoshouzhen.blog.blogmsauth.utils.RsaUtil;

import javax.annotation.Resource;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 模块测试
 * @date 2022/5/28 1:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ModuleTests {
    @Resource
    private RoleServiceImpl roleService;

    /**
     * 日志功能测试
     */
    @Test
    public void testLog(){
        log.error("test log");
    }

    /**
     * mysql测试
     */
    @Test
    public void testMySql(){
        Role role = roleService.selectByPrimaryKey(1);
        Assert.notNull(role, "测试查询数据库失败");
    }

    /**
     * redis测试
     */
    @Test
    public void testRedis(){
        String key = "test-Key";
        String val = "test-Value";
        RedisUtil.set(key, val);
        System.out.println(RedisUtil.get(key));
        RedisUtil.delKeys(key);
    }
}
