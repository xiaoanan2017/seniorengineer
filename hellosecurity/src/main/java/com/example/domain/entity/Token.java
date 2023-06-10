package com.example.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author xiaoaa
 * @date 2023/5/20 15:45
 **/
@Data
@Builder
public class Token {

    private String token;

    private Date expireTime;
}
