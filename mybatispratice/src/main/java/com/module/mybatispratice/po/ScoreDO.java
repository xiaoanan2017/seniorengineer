package com.module.mybatispratice.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaoaa
 * @date 2022/8/10 22:14
 **/
@Data
public class ScoreDO implements Serializable {

    private static final long serialVersionUID = 5525641076834668445L;

    private Long id;
    private Long studentId;
    private String subject;
    private Integer score;
}
