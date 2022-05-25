package top.guoshouzhen.blog.blogmsauth.config.db;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Mybatis配置类
 *
 * @author guoshouzhen
 * @date 2021/11/20 14:51
 * @return null
 */
@Configuration
@MapperScan("top.guoshouzhen.blog.blogmsauth.mapper")
public class MybatisConfig {
    /**
     * mybatis sql配置文件路径
     */
    @Value("${mybatis.mapper-locations}")
    private String mapperLoc;

    /**
     * 数据实体类包名
     */
    @Value("${mybatis.type-aliases-package}")
    private String typeAliaseLoc;

    /**
     * 配置Mybatis SqlSesstionFactory
     *
     * @param dataSource 数据源
     * @return org.apache.ibatis.session.SqlSessionFactory
     * @author guoshouzhen
     * @date 2021/11/21 12:56
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        VFS.addImplClass(SpringBootVFS.class);

        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeAliasesPackage(typeAliaseLoc);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver
                ().getResources(mapperLoc));
        return sessionFactoryBean.getObject();
    }
}
