package top.guoshouzhen.blog.blogmsauth.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 从spring容器中获取bean的工具类
 *
 * @author GuoShouzhen
 * @date 2021/12/15 11:59
 */
@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据name和class获取bean
     *
     * @param name  bean name
     * @param clazz class
     * @return T
     * @author GuoShouzhen
     * @date 2021/12/15 11:57
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        ApplicationContext ac = getApplicationContext();
        if (ac == null) {
            log.error("ApplicationContext注入失败");
            return null;
        }
        return ac.getBean(name, clazz);
    }

    /**
     * 根据class获取bean
     *
     * @param clazz class
     * @return T
     * @author GuoShouzhen
     * @date 2021/12/15 11:59
     */
    public static <T> T getBean(Class<T> clazz) {
        ApplicationContext ac = getApplicationContext();
        if (ac == null) {
            log.error("ApplicationContext注入失败");
            return null;
        }
        return ac.getBean(clazz);
    }

}
