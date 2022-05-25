package top.guoshouzhen.blog.blogmsauth.config.db.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.guoshouzhen.blog.blogmsauth.config.properties.DruidProperties;
import top.guoshouzhen.blog.blogmsauth.model.enums.DataSourceEnum;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 * @author shouzhen.guo
 * @date 2022/1/19 15:43
 */
@Configuration
public class DataSourceConfig {
    /**
     * 配置主数据源
     *
     * @return javax.sql.DataSource
     * @author guoshouzhen
     * @date 2021/11/20 22:06
     */
    @Bean("masterDataSource")
    @ConfigurationProperties("spring.datasource.druid.blog1")
    public DataSource masterDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        // config datasource
        return druidProperties.setDataSourceProps(dataSource);
    }

    /**
     * 配置从数据源
     * 配置为不生效时，bean无法被注入
     * @return javax.sql.DataSource
     * @author guoshouzhen
     * @date 2021/11/20 22:10
     */
    @Bean("slaveDataSource")
    @ConfigurationProperties("spring.datasource.druid.blog2")
    public DataSource slaveDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        // config datasource
        return druidProperties.setDataSourceProps(dataSource);
    }

    /**
     * 向数据源路由注入数据源，并返回路由实例
     * 暂时仅使用一个库
     * @return top.guoshouzhen.cloud.config.db.datasource.DynamicRoutingDataSource
     * @author guoshouzhen
     * @date 2021/11/21 1:04
     */
    @Primary
    @Bean(name = "dynamicRoutingDataSource")
    public DynamicRoutingDataSource dynamicRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDs,
                                                             @Qualifier("slaveDataSource") DataSource slaveDs) {
        // 数据源
        Map<Object, Object> mapTargetDs = new HashMap<Object, Object>() {
            {
                put(DataSourceEnum.MASTER.getName(), masterDs);
                put(DataSourceEnum.SLAVE.getName(), slaveDs);
            }
        };
        return new DynamicRoutingDataSource(masterDs, mapTargetDs);
    }
}
