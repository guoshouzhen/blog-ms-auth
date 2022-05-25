package top.guoshouzhen.blog.blogmsauth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import top.guoshouzhen.blog.blogmsauth.model.enums.ErrorCodeEnum;
import top.guoshouzhen.blog.blogmsauth.model.vo.Result;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 全局异常处理器
 * RestControllerAdvice注解可全局捕获spring mvc抛的异常
 * @date 2021/12/28 19:55
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    /**
     * 拦截Exception的异常处理方法
     *
     * @param ex            Exception异常
     * @param handlerMethod 一个包含方法信息信息的对象
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.Result<java.lang.Object>
     * @author shouzhen.guo
     * @date 2021/12/28 20:00
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex, HandlerMethod handlerMethod) {
        log.error("请求失败，程序异常，异常信息：", ex);
        return Result.fail(ErrorCodeEnum.A0000.getErrCode(), ErrorCodeEnum.A0000.getErrMsg());
    }

    /**
     * 拦截ServiceException的异常处理方法
     *
     * @param ex            ServiceException
     * @param handlerMethod 一个包含方法信息信息的对象
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.Result<java.lang.Object>
     * @author shouzhen.guo
     * @date 2021/12/28 20:06
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException ex, HandlerMethod handlerMethod) {
        //业务异常根据传入参数判定是否需要打印堆栈信息
        if(ex.isRecordStackInfos()){
            log.error("请求失败，业务异常，异常信息：", ex);
        }
        if (ex.getData() != null) {
            return Result.fail(ex.getErrCode(), ex.getErrMsg(), ex.getData());
        }
        return Result.fail(ex.getErrCode(), ex.getErrMsg());
    }

    /**
     * spring 参数验证异常处理
     * @author shouzhen.guo
     * @date 2022/5/16 1:21
     * @param ex 参数验证异常
     * @param handlerMethod 一个包含方法信息信息的对象
     * @return top.guoshouzhen.blog.blogmsauth.model.vo.Result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HandlerMethod handlerMethod){
        //参数验证错误
        log.error("接口参数验证错误，错误信息：{}", ex.getMessage());
        return Result.fail(ErrorCodeEnum.A0001.getErrCode(), ErrorCodeEnum.A0001.getErrMsg());
    }
}
