package com.example.domain.controller;

import com.example.domain.entity.Result;
import com.example.domain.entity.Token;
import com.example.domain.entity.User;
import com.example.domain.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 登录控制器
 * @author xiaoaa
 * @date 2023/5/9 21:34
 **/
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 1.自定义登录接口：
     *      借助 AuthenticationManager 调用 ProviderManager 的方法进行认证，如果认证通过生成 jwt
     *      把用户信息存入redis
     *
     */
    @RequestMapping("/login")
    public Result<Token> login(@RequestBody User user) {

        return loginService.login(user);
    }

    @RequestMapping("/logout")
    public Result<String> logout() {

        return loginService.logout();
    }

    @RequestMapping("/hello")
    public Result<String> hello() {

        return Result.success("hello是个普通接口，需要鉴权\r 你好啊");
    }


    @RequestMapping("/test")
    public Result<String> test() {

        return Result.success("test接口不需要鉴权，可以直接被访问\r 你好啊");
    }

    @RequestMapping("/denyAll")
    public Result<String> denyAll() {

        return Result.success("不论是否有token，denyAll 接口都会直接被拒绝访问");
    }

    @RequestMapping("/tellAuthority")
    @PreAuthorize("hasAuthority('test')")
    public Result<String> tellAuthority() {

        return Result.success("tellAuthority，需要用户具有 test权限才可以访问");
    }

}
