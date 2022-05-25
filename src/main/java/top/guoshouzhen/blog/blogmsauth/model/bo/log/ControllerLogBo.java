package top.guoshouzhen.blog.blogmsauth.model.bo.log;

import lombok.Data;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 请求监控日志
 * @date 2022/5/14 22:10
 */
@Data
public class ControllerLogBo {
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 请求路径
     */
    private String requestPath;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 花费时间
     */
    private double costTime;
    /**
     * 请求是否成功
     */
    private boolean isSuccess;
    /**
     * 请求响应
     */
    private String response;
    /**
     * 异常信息
     */
    private String exceptionMsg;

    @Override
    public String toString(){
        return String.format( "api请求日志：\n" +
                "类名：%s\n" +
                "方法名：%s\n" +
                "请求路径：%s\n" +
                "请求方式：%s\n" +
                "请求参数：%s\n" +
                "花费时间：%sms\n" +
                "请求：%s\n" +
                "响应结果：%s\n" +
                "异常信息：%s\r\n", className, methodName, requestPath, requestMethod, requestParams, costTime, isSuccess ? "成功":"失败", response, exceptionMsg);
    }
}
