package top.guoshouzhen.blog.blogmsauth.config.db.datasource;

import lombok.extern.slf4j.Slf4j;
import top.guoshouzhen.blog.blogmsauth.utils.StringUtil;

/**
 * 提供数据源切换的方法
 * 内部维护了一个static变量，用于记录每个线程需要使用的数据源关键字，
 * 并提供切换、读取、和清楚数据源配置信息的方法
 * @author guoshouzhen
 * @date 2021/11/20 23:26
 * @return null
 */
@Slf4j
public class DynamicDataSourceContextHolder {
    /**
     * ThreadLocal：线程局部变量，多个线程使用一个初始值，每个线程都有一个变量副本，互相隔离
     */
    private static final ThreadLocal<String> THREAD_LOCAL_DS_CONTEXT = new ThreadLocal<>();

    /**
     * 切换当前线程的数据源（通过数据库名称）
     * @author guoshouzhen
     * @date 2021/11/21 0:06
     * @param dataSourceKey 数据源类型（数据源key）
     */
    public static void switchDataSource(String dataSourceKey){
        THREAD_LOCAL_DS_CONTEXT.set(dataSourceKey);
        log.info("DataSource Changed：{}", dataSourceKey);
    }

    /**
     * 获取当前线程的数据源key
     * @author guoshouzhen
     * @date 2021/11/21 0:08
     * @return java.lang.String
     */
    public static String getDataSourceKey(){
        return THREAD_LOCAL_DS_CONTEXT.get();
    }

    /**
     * 清楚当前线程的数据源配置信息
     * @author guoshouzhen
     * @date 2021/11/21 0:09
     */
    public static void clear(){
        String strDsName = THREAD_LOCAL_DS_CONTEXT.get();
        if(!StringUtil.isNullOrWhiteSpace(strDsName)) {
            log.info("DataSource Removed：{}", strDsName);
            THREAD_LOCAL_DS_CONTEXT.remove();
        }
    }
}
