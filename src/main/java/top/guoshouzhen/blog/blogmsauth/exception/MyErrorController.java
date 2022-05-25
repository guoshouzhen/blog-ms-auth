package top.guoshouzhen.blog.blogmsauth.exception;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import top.guoshouzhen.blog.blogmsauth.model.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 自定义ErrorController实现，拦截未进入控制器就抛出的异常
 * @date 2022/1/21 23:31
 */
@RestController
@AllArgsConstructor
@Slf4j
public class MyErrorController implements ErrorController {
    private final ErrorAttributes errorAttributes;

    /**
     * 接口该方法已过时，返回null应该就可以
     *
     * @return java.lang.String
     * @author guoshouzhen
     * @date 2022/1/22 0:03
     */
    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping("/error")
    public Result error(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> body = this.errorAttributes.getErrorAttributes(new ServletWebRequest(request), ErrorAttributeOptions.defaults());
        response.setStatus(HttpStatus.OK.value());
        int statusCode = Integer.parseInt(body.get("status").toString());
        log.error("请求错误，请求路径：{}，请求状态：{}，错误信息：{}", body.get("path"), statusCode, body.get("error"));
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        return Result.fail(String.valueOf(httpStatus.value()), httpStatus.getReasonPhrase());
    }
}
