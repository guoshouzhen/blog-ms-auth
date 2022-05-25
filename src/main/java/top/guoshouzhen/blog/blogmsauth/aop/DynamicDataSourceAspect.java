package top.guoshouzhen.blog.blogmsauth.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.guoshouzhen.blog.blogmsauth.config.db.datasource.DynamicDataSourceContextHolder;
import top.guoshouzhen.blog.blogmsauth.model.annotations.DatabaseSource;

import java.lang.reflect.Method;


/**
 * 切面类
 * 使用AOP切入DataSource注解，实现数据源切换
 *
 * @author guoshouzhen
 * @date 2021/11/21 13:48
 */
@Aspect //把当前类标识为一个切面供容器读取
@Order(1) //指定Spring容器加载Bean的顺序，接收一个int，值越小优先级越高
@Component
public class DynamicDataSourceAspect {

    /**
     * 定义切入点为DataSource注解
     *
     * @author guoshouzhen
     * @date 2021/11/21 14:10
     */
    @Pointcut("@annotation(top.guoshouzhen.blog.blogmsauth.model.annotations.DatabaseSource) || @within(top.guoshouzhen.blog.blogmsauth.model.annotations.DatabaseSource))")
    public void dsPointCut() {
    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取数据源注解对象
        DatabaseSource dataSource = getDataSource(joinPoint);
        if (dataSource != null) {
            // 根据注解指定的value切换数据源
            DynamicDataSourceContextHolder.switchDataSource(dataSource.value().getName());
        }
        try {
            return joinPoint.proceed();
        } finally {
            // 方法执行完毕，清除当前线程持有的数据源
            DynamicDataSourceContextHolder.clear();
        }
    }

    /**
     * 从切入点（DataSource注解修饰的类或者方法）获取DataSource注解对象
     *
     * @param joinPoint 切入点
     * @return top.guoshouzhen.cloud.model.annotation.DataSource
     * @author guoshouzhen
     * @date 2021/11/21 14:28
     */
    public DatabaseSource getDataSource(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        DatabaseSource targetDataSourceAnno = targetClass.getAnnotation(DatabaseSource.class);
        if (targetDataSourceAnno == null) {
            Method method = signature.getMethod();
            targetDataSourceAnno = method.getAnnotation(DatabaseSource.class);
        }
        return targetDataSourceAnno;
    }
}
