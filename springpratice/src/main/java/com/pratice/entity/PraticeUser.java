package com.pratice.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author xiaoaa
 * @date 2022/9/6 21:39
 **/
@Data
public class PraticeUser  implements UserDetails, Serializable {

    private static final long serialVersionUID = 5881889607447324624L;

    private Long id;
    private String username;
    private String password;

    private List<PraticeRole> authroities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authroities;
    }

    //用户账号是否过期
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    //用户账号是否被锁定
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
    //用户密码是否过期
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    //用户是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
