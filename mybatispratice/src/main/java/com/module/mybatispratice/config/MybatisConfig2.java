package com.module.mybatispratice.config;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 另外一种更便捷的方式导入 mybatis参数
 * mybatis的参数可以全部注入到 MybatisProperties
 * @author xiaoaa
 * @date 2022/7/27 22:37
 **/
@Configuration
public class MybatisConfig2 {

    @Resource
    private DataSource dataSource;

    //以下三种方式都可以自动将 Interceptor 接口的全部实现类的实例注入进来

    @Autowired
    private Interceptor[] interceptors;

    @Autowired
    private List<Interceptor> interceptorList;

    @Autowired
    private Map<String,Interceptor> interceptorMap;

    @Bean
    @Primary
    public SqlSessionFactoryBean factoryBean(MybatisProperties mybatisProperties) throws Exception {
        //自定义 SqlSessionFactoryBean 一般提供set方法的都是需要额外再自行设置的 ，否则直接装到 configuration 对象里了
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setConfiguration(mybatisProperties.getConfiguration());
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(mybatisProperties.resolveMapperLocations());
        factoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        //yml文件中的很多参数看着跟 SqlSessionFactoryBean 中的属性名一直，可是如果是自定义了 SqlSessionFactoryBean ，实际上是无法直接通过属性赋值的
        //可以借用 MybatisProperties 来进行设值

        //将全部拦截器设置进来
        factoryBean.setPlugins(interceptors);
        return factoryBean;
    }

    /**
     * 以 mybatis中 执行 query为例对流程进行梳理如下：
     * 1、Executor执行query()方法，创建一个StatementHandler对象
     * 2、StatementHandler 调用ParameterHandler对象的setParameters()方法
     * 3、StatementHandler 调用 Statement对象的execute()方法
     * 4、StatementHandler 调用ResultSetHandler对象的handleResultSets()方法，返回最终结果
     */


}
