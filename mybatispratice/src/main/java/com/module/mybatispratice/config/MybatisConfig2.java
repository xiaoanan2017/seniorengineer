package com.module.mybatispratice.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 另外一种更便捷的方式导入 mybatis参数
 * mybatis的参数可以全部注入到 MybatisProperties
 * @author xiaoaa
 * @date 2022/7/27 22:37
 **/
@Configuration
public class MybatisConfig2 {

    @javax.annotation.Resource
    private DataSource dataSource;

    @Bean
    @Primary
    public SqlSessionFactoryBean factoryBean(MybatisProperties mybatisProperties) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setConfiguration(mybatisProperties.getConfiguration());
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(mybatisProperties.resolveMapperLocations());
        factoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        //yml文件中的很多参数看着跟 SqlSessionFactoryBean 中的属性名一直，可是如果是自定义了 SqlSessionFactoryBean ，实际上是无法直接通过属性赋值的
        //可以借用 MybatisProperties 来进行设值
        return factoryBean;
    }

}
