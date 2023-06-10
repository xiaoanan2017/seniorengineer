package com.example.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaoaa
 * @date 2023/5/9 23:48
 **/
@Data
public class User implements Serializable {

    private Long id;

    private String userName;

    private String password;


}
