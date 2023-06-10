package com.pratice.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author xiaoaa
 * @date 2022/9/6 21:42
 **/
@Data
public class PraticeRole implements GrantedAuthority {

    private Long id;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
