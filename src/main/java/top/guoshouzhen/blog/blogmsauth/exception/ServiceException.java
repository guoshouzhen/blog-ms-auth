package top.guoshouzhen.blog.blogmsauth.exception;

import top.guoshouzhen.blog.blogmsauth.model.enums.ErrorCodeEnum;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 自定义业务用异常
 * 注：
 * 在业务代码中使用异常抛出的方式来控制返回会带来性能问题：
 * 抛出异常时，jvm会追踪堆栈信息，创建异常，性能开销远大于return一个业务状态标记，但前者好维护，后者可读性差
 * 需要考虑取舍
 * 一种解决方案：
 * 创建异常时指定不追踪堆栈信息
 * throwable构造方法会根据传入的相关参数是否追踪并记录堆栈信息（相关的操作是线程同步的）
 * @date 2021/12/28 19:29
 */
public class ServiceException extends RuntimeException {
    /**
     * 自定义错误code
     */
    private final String errCode;
    /**
     * 自定义错误消息
     */
    private final String errMsg;
    /**
     * 返回给前端的数据
     */
    private final Object data;
    /**
     * 创建异常时是否进行栈追踪
     */
    private final boolean isRecordStackInfos;

    /**
     * 获取错误码
     *
     * @return java.lang.String
     * @author shouzhen.guo
     * @date 2022/3/14 13:48
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * 获取错误消息
     *
     * @return java.lang.String
     * @author shouzhen.guo
     * @date 2022/3/14 13:49
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * 获取响应数据
     *
     * @return java.lang.Object
     * @author shouzhen.guo
     * @date 2022/3/14 13:49
     */
    public Object getData() {
        return data;
    }

    /**
     * 获取是否追踪栈信息的设置
     *
     * @return boolean
     * @author shouzhen.guo
     * @date 2022/3/14 13:49
     */
    public boolean isRecordStackInfos() {
        return isRecordStackInfos;
    }

    /**
     * 构造器
     *
     * @param errorCodeEnum      错误枚举对象
     * @param isRecordStackInfos 是否进行栈追踪
     * @author shouzhen.guo
     * @date 2022/3/14 13:45
     */
    public ServiceException(ErrorCodeEnum errorCodeEnum, boolean isRecordStackInfos) {
        super(errorCodeEnum.getErrMsg(), null, false, isRecordStackInfos);
        this.errCode = errorCodeEnum.getErrCode();
        this.errMsg = errorCodeEnum.getErrMsg();
        this.data = null;
        this.isRecordStackInfos = isRecordStackInfos;
    }

    /**
     * 重载，默认不会追踪栈信息
     *
     * @param errorCodeEnum 错误枚举对象
     */
    public ServiceException(ErrorCodeEnum errorCodeEnum) {
        this(errorCodeEnum, false);
    }

    /**
     * 构造器
     *
     * @param errCode            错误码
     * @param errMsg             错误消息
     * @param isRecordStackInfos 是否追踪栈信息
     * @author shouzhen.guo
     * @date 2022/3/14 14:17
     */
    public ServiceException(String errCode, String errMsg, boolean isRecordStackInfos) {
        super(errMsg, null, false, isRecordStackInfos);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.data = null;
        this.isRecordStackInfos = isRecordStackInfos;
    }

    /**
     * 重载，默认不会追踪栈信息
     *
     * @param errCode 错误码
     * @param errMsg  错误消息
     */
    public ServiceException(String errCode, String errMsg) {
        this(errCode, errMsg, false);
    }
}
