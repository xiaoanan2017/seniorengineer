package com.example.domain.service;

import com.example.domain.entity.LoginUser;
import com.example.domain.entity.User;
import com.example.domain.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义UserDetailService
 *  去数据库中查询用户数据
 * @author xiaoaa
 * @date 2023/5/9 23:57
 **/

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        User user = userMapper.getUser(username);
        // if (Objects.isNull(user)) {
        //     //抛出异常之后，后续拦截器会继续处理
        //     throw new BizException("用户名或密码错误");
        // }
        //todo 查询对应的权限信息
        List<String> list = new ArrayList<>(Arrays.asList("test", "admin"));
        LoginUser loginUser = new LoginUser(user, list);
        return loginUser;
    }

}
