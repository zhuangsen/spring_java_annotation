package com.zs.spring.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-28 17:30
 * @description:
 */
//@Configuration
@Slf4j
//@ComponentScan("com.zs.spring")
//@PropertySource({"classpath:config.properties"})
//@MapperScan(basePackages = {"com.zs.spring.mapper"})
//@EnableTransactionManagement
public class HikariDataSourceConfig {
    @Value("${jdbc.jdbcUrl}")
    private String url;
    @Value("${jdbc.driverClass}")
    private String driver;
    @Value("${jdbc.user}")
    private String user;
    @Value("${jdbc.password}")
    private String password;
    @Value("${mybatis.type.alias.package}")
    private String mybatisTypeAliasPackage;

    /**
     * 配置数据源
     *
     * @date 2018/6/24
     **/
    @Bean
    public HikariDataSource hikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(user);
        hikariDataSource.setPassword(password);
        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setDriverClassName(driver);
        return hikariDataSource;
    }

    /**
     * 配置mybatis的SqlSessionFactoryBean
     *
     * @param hikariDataSource
     * @return
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(HikariDataSource hikariDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(hikariDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisTypeAliasPackage);
        // 动态获取SqlMapper
//        PathMatchingResourcePatternResolver classPathResource = new PathMatchingResourcePatternResolver();
//        sqlSessionFactoryBean.setMapperLocations(classPathResource.getResources("com.zs.spring.mapper"));

        return sqlSessionFactoryBean;
    }

    @Bean
    public DynamicDataSource dynamicDataSource(HikariDataSource hikariDataSource){
        log.info("=========dynamicDataSource========");
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map dataSourceMap = new HashMap<>();
        dataSourceMap.put("dataSource",hikariDataSource);
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(hikariDataSource);
        return dynamicDataSource;
    }

    /**
     * 配置spring的声明式事务
     *
     * @return
     */

    @Bean
    public PlatformTransactionManager transactionManager(HikariDataSource hikariDataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(hikariDataSource);
        return dataSourceTransactionManager;

    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        return propertySourcesPlaceholderConfigurer;
    }
}
