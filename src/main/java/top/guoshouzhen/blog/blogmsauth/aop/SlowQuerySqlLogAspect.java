package top.guoshouzhen.blog.blogmsauth.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.StringJoiner;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 切面类，记录慢sql日志
 * @date 2021/12/30 13:03
 */
@Aspect
@Component
@Slf4j
public class SlowQuerySqlLogAspect {
    /**
     * 是否开启慢sql记录
     */
    @Value("${sqllog.open:false}")
    private boolean isOpenSlowQueryLog;
    /**
     * 慢sql时间（毫秒）
     */
    @Value("${sqllog.limittime:2000}")
    private Long slowQueryLimitTime;

    /**
     * 声明切入点
     */
    @Pointcut(value = "execution(public * top.guoshouzhen.blog.blogmsauth.mapper..*.*(..))")
    public void doPointCut() {
    }

    @Around(value = "doPointCut()")

    public Object mapperAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!isOpenSlowQueryLog) {
            return joinPoint.proceed();
        }
        long lStartTime = System.nanoTime();
        //执行查询
        Object object = joinPoint.proceed();
        //计算花费时间（毫秒）
        double lCostTime = (System.nanoTime() - lStartTime) / 1000000.0;
        long lLimitTime = slowQueryLimitTime;
        if (lCostTime > lLimitTime) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            Object[] args = joinPoint.getArgs();
            StringJoiner params = new StringJoiner(",");
            for (Object arg : args) {
                params.add(String.valueOf(arg));
            }
            //打印日志
            log.error("慢SQL日志，执行时间：{}ms，类名：{}，方法名：{}，请求参数：{}。",
                    lCostTime,
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    params.toString()
            );
        }
        return object;

    }


}
