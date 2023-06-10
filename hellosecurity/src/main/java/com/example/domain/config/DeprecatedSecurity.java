package com.example.domain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 默认登录方式配置是在 SpringBootWebSecurityConfiguration  中
 * 该方式不推荐，只做演示
 * 通过继承 WebSecurityConfigurerAdapter 可以重新配置url规则
 * @author xiaoaa
 * @date 2023/5/9 22:17
 **/
//@Configuration
public class DeprecatedSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 配置url 规则 ，登录方式等
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //访问 admin 需要鉴权且用户需要有Admin角色 ，
        // 所有url都需要鉴权
        http.authorizeRequests().antMatchers("/admin/**").hasAnyRole("Admin")
            .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }

    /**
     * 配置 用户角色
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(securityProperties.getUser().getName()).password("{noop}".concat(securityProperties.getUser().getPassword())).roles("Admin");
//        super.configure(auth);
    }

    /**
     * 配置用户服务
     * 如何获取用户
     * @return
     */
    @Override
    protected UserDetailsService userDetailsService() {

        return super.userDetailsService();
    }


}
