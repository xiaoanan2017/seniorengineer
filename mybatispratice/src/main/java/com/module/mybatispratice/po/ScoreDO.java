package com.module.mybatispratice.po;

import lombok.Data;

/**
 * @author xiaoaa
 * @date 2022/8/10 22:14
 **/
@Data
public class ScoreDO {

    private Long id;
    private Long studentId;
    private String subject;
    private Integer score;
}
