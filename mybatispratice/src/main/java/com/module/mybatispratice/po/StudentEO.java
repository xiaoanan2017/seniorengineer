package com.module.mybatispratice.po;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author xiaoaa
 * @date 2022/8/10 22:11
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentEO extends StudentDO{

    private ScoreDO scoreDO;

    private List<ScoreDO> list;
}
