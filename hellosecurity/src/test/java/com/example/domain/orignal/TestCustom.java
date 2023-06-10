package com.example.domain.orignal;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 不需要启动 springBoot的测试类
 * @author xiaoaa
 * @date 2023/5/20 15:29
 **/
public class TestCustom {

    @Test
    public void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "1234";
        String encode = passwordEncoder.encode(password);
        String encode1 = passwordEncoder.encode(password);
        System.out.println(encode);
        System.out.println(encode1);

        System.out.println(passwordEncoder.matches(password, encode));
    }


}
