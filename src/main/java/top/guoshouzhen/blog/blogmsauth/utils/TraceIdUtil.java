package top.guoshouzhen.blog.blogmsauth.utils;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description TraceId封装
 * @date 2022/3/1 18:46
 */
public class TraceIdUtil {
    private static final String TRACE_ID = "TRACE_ID";

    /**
     * 获取trace id
     *
     * @return java.lang.String
     * @author shouzhen.guo
     * @date 2022/3/1 18:54
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    /**
     * 向线程本地变量（MDC底层使用ThreadLocal实现）写入trace id
     *
     * @author shouzhen.guo
     * @date 2022/3/1 18:53
     */
    public static void setTraceId() {
        if (StringUtil.isNullOrWhiteSpace(MDC.get(TRACE_ID))) {
            MDC.put(TRACE_ID, createTraceId());
        }
    }

    /**
     * 创建UUID作为请求ID
     *
     * @return java.lang.String
     * @author shouzhen.guo
     * @date 2022/3/1 18:52
     */
    public static String createTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 清除线程变量
     *
     * @author shouzhen.guo
     * @date 2022/3/1 19:09
     */
    public static void clear() {
        MDC.remove(TRACE_ID);
    }
}
