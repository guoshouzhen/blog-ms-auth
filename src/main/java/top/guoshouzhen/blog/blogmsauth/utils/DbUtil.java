package top.guoshouzhen.blog.blogmsauth.utils;

import top.guoshouzhen.blog.blogmsauth.config.db.datasource.DynamicDataSourceContextHolder;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 数据库相关的工具类
 * @date 2022/3/4 18:12
 */
public class DbUtil {
    /**
     * 数据库名称前缀
     */
    private static final String DB_NAME_PREFIX = "blog";

    /**
     * 切换当前线程的数据源（通过数据库名称）
     * @author shouzhen.guo
     * @date 2022/3/4 18:15
     * @param dbId 数据库ID
     */
    public static void switchDateSource(String dbId){
        String dataSourceName = DB_NAME_PREFIX + dbId;
        DynamicDataSourceContextHolder.switchDataSource(dataSourceName);
    }

    /**
     * 重置数据源配置
     * @author shouzhen.guo
     * @date 2022/5/15 23:18
     */
    public static void resetDataSource(){
        DynamicDataSourceContextHolder.clear();
    }
}
