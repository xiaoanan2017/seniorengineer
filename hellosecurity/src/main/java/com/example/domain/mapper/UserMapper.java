package com.example.domain.mapper;

import com.example.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoaa
 * @date 2023/5/9 23:59
 **/
@Mapper
public interface UserMapper{

    User getUser(String userName);

    User getUserById(Long id);

    
}

