package com.example.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 静态资源配置
 * @author xiaoaa
 * @date 2023/5/9 23:15
 **/

@Configuration
public class MvcSupportConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/html/**")
                //不需要再带 resources 这一层了，因为 resources 这个路径 == classpath
                .addResourceLocations( "classpath:/html/");
    }

    // 配置跨域
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        // 允许跨域的路径
        registry.addMapping("/**")
                // 允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 允许的请求方法
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}

