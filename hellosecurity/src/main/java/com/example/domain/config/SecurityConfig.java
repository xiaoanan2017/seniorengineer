package com.example.domain.config;

import com.example.domain.filter.TokenFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 推荐的  SecurityConfig 配置
 * @author xiaoaa
 * @date 2023/5/9 22:43
 **/
@Configuration
@EnableWebSecurity
@Slf4j
// 开启授权限制
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private AuthenticationConfiguration configuration;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @SneakyThrows
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        AuthenticationManager authenticationManager = configuration.getAuthenticationManager();
        return authenticationManager;
    }
    /**
     * url拦截规则
     * 这个拦截链是可以定义多个bean的
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        //使用由httpSecurityConfiguration 装配的 HttpSecurity 则会紧密的保持原有的过滤器
        http.csrf().disable()//前后端分离项目要关闭 csrf
                //登录接口只允许匿名访问（即未登录状态下才可以访问）
            .authorizeRequests().antMatchers("/user/login").anonymous()
                //登出接口需要鉴权才能访问
            .antMatchers("/user/logout").authenticated()
                //test 在任何情况下都可以访问
            .antMatchers("/user/test", "/error").permitAll()
                //denyAll 在任何情况下都不可以访问
            .antMatchers("/user/denyAll").denyAll()
                //其余接口都需要鉴权
            .anyRequest().authenticated()
                //formLogin 添加了 usernamePasswordAuthenticationFilter  登录页等信息
            //.formLogin().and()
            .and()
                //任何情况下都不创建 http session ，即关闭session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 配置异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        //另外一种是直接返回 SecurityFilterChain 往里面添加Filter即可
        //允许跨域
        // http.cors();
        return http.build();
    }


    /**
     * url放行规则
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring().antMatchers("/html/**");
    }

    /**
     * 必须配置一种加密方式
     *
     * @return
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


}
