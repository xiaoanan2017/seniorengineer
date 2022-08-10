package com.module.mybatispratice.config;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author xiaoaa
 * @date 2022/7/27 22:37
 **/
@Configuration
public class MybatisConfig {

    @javax.annotation.Resource
    private DataSource dataSource;

    @Bean
    @Primary
    public SqlSessionFactoryBean factoryBean(org.apache.ibatis.session.Configuration config) throws IOException {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfiguration(config);
        factoryBean.setTypeAliasesPackage("com.module.mybatispratice.po");
        // classpath 当前类加载器路径  classpath* 所有类加载器路径
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/templates/mappers/*.xml");
        factoryBean.setMapperLocations(resources);

        return factoryBean;
    }
    
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration config() {

        return new org.apache.ibatis.session.Configuration();
    }

}
