package top.guoshouzhen.blog.blogmsauth.interceptor;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import top.guoshouzhen.blog.blogmsauth.config.db.datasource.DynamicDataSourceContextHolder;
import top.guoshouzhen.blog.blogmsauth.utils.TraceIdUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 通用拦截器（拦截所有请求）
 * @date 2022/3/1 18:57
 */
public class CommonInterceptor implements HandlerInterceptor {
    /**
     * 请求处理前，在执行controller方法之前
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return boolean
     * @author shouzhen.guo
     * @date 2022/3/1 19:10
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.所有请求添加唯一的日志追踪ID
        TraceIdUtil.setTraceId();
        //2.TODO　加密签名验证
        return true;
    }

    /**
     * 请求处理完后，执行完controller并return后，主要做资源清理
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @param ex       ex
     * @author shouzhen.guo
     * @date 2022/3/1 19:12
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        DynamicDataSourceContextHolder.clear();
        TraceIdUtil.clear();
    }
}
