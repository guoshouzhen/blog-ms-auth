package top.guoshouzhen.blog.blogmsauth.config.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.guoshouzhen.blog.blogmsauth.interceptor.CommonInterceptor;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description spring mvc相关配置，实现WebMvcConfigure接口
 * @date 2022/3/1 18:55
 */
@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //通用拦截器，拦截所有请求
        registry.addInterceptor(new CommonInterceptor()).addPathPatterns("/**");
        log.info("Path [/**] added interceptor [{}]", CommonInterceptor.class.getName());

        //TODO 供内部访问接口（/innerapi/**）拦截器，需要验证IP为内网IP（服务调用全部通过域名走网关），暂不实现
    }
}
