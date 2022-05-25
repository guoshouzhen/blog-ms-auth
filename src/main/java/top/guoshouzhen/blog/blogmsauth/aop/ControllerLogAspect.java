package top.guoshouzhen.blog.blogmsauth.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.guoshouzhen.blog.blogmsauth.model.bo.log.ControllerLogBo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.StringJoiner;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 记录接口请求日志的切面类
 * @date 2021/12/30 15:20
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAspect {
    /**
     * 切入点
     */
    @Pointcut(value = "execution(public * top.guoshouzhen.blog.blogmsauth.controller..*.*(..))")
    public void doPoint() {
    }

    @Around(value = "doPoint()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        boolean isPostMapping = method.isAnnotationPresent(PostMapping.class),
                isGetMapping = method.isAnnotationPresent(GetMapping.class),
                isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
        if (isPostMapping || isGetMapping || isRequestMapping) {
            ControllerLogBo controllerLogBo = new ControllerLogBo();
            long lStartTime = System.nanoTime();
            Object[] args = joinPoint.getArgs();
            StringJoiner params = new StringJoiner(",");
            for (Object arg : args) {
                params.add(String.valueOf(arg));
            }
            //获取当前Http请求实例，此方式注意不能在子线程中使用，若请求处理完毕被销毁后，则在子线程中可能获取不到request实例
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            controllerLogBo.setClassName(method.getDeclaringClass().getName());
            controllerLogBo.setMethodName(method.getName());
            controllerLogBo.setRequestPath(new String(httpServletRequest.getRequestURL()));
            controllerLogBo.setRequestMethod(httpServletRequest.getMethod());
            controllerLogBo.setRequestParams(params.toString());
            try{
                Object object = joinPoint.proceed();
                double lCostTime = (System.nanoTime() - lStartTime) / 1000000.0;
                controllerLogBo.setCostTime(lCostTime);
                controllerLogBo.setSuccess(true);
                controllerLogBo.setResponse(object.toString());
                controllerLogBo.setExceptionMsg("无");
                log.info(controllerLogBo.toString());
                return object;
            }catch (Throwable ex){
                double lCostTime = (System.nanoTime() - lStartTime) / 1000000.0;
                controllerLogBo.setCostTime(lCostTime);
                controllerLogBo.setSuccess(false);
                controllerLogBo.setResponse("");
                controllerLogBo.setExceptionMsg(ex.getMessage());
                log.info(controllerLogBo.toString());
                throw ex;
            }
        }
        return joinPoint.proceed();
    }
}
