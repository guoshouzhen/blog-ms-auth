package top.guoshouzhen.blog.blogmsauth.config.db.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 继承了AbstractRoutingDataSource类（该类继承自AbstractDataSource，内部实现了DataSource接口获取数据库连接的方法），其内部维护一个targetDataSources的Map，
 * 通过setter设置数据源关键字和数据源的关系，实现类需要具体实现determineCurrentLookupKey方法，此方法的返回值决定具体从哪个数据源中获取连接
 * 使用方式：
 * 可在程序运行时动态切换数据源，在数据层标注需要访问数据源的关键字，路由到该数据源，获取连接
 * @author guoshouzhen
 * @date 2021/11/20 23:19
 * @return null
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    public DynamicRoutingDataSource(DataSource defaultTargetDataSource, Map<Object, Object> mapTargetDataSources){
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(mapTargetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
}
