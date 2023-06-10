package com.pratice.controller;

import com.pratice.entity.PraticeUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoaa
 * @date 2022/9/16 21:11
 **/
@RestController
@RequestMapping("/basic")
public class BasicController {

    @RequestMapping("/login")
    public PraticeUser login(){
        PraticeUser praticeUser = new PraticeUser();
        praticeUser.setUsername("user");
        praticeUser.setPassword("password");
        return praticeUser;
    }

    @RequestMapping("/test")
    public PraticeUser test(){
        PraticeUser praticeUser = new PraticeUser();
        praticeUser.setUsername("test");
        praticeUser.setPassword("test");
        return praticeUser;
    }
}
