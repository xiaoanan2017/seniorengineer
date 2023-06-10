package com.pratice.serviceImpl;

import com.pratice.entity.PraticeRole;
import com.pratice.entity.PraticeUser;
import com.pratice.mapper.PraticeRoleMapper;
import com.pratice.mapper.PraticeUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Objects;

/**
 * @author xiaoaa
 * @date 2022/8/18 7:29
 **/
//@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PraticeUserMapper praticeUserMapper;

    @Autowired
    private PraticeRoleMapper praticeRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查数据库
        PraticeUser praticeUser = praticeUserMapper.loadUserByUsername(username);
        if (Objects.nonNull(praticeUser)) {
            List<PraticeRole> roles = praticeRoleMapper.getRolesByUserId(praticeUser.getId());
            praticeUser.setAuthroities(roles);
        }
        return praticeUser;
    }


    
}
