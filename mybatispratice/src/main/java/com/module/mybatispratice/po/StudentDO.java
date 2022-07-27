package com.module.mybatispratice.po;

import lombok.Builder;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author xiaoaa
 * @date 2022/7/27 22:57
 **/
@Table(name =  "student")
@Data
@Builder
public class StudentDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    private String address;

    private String ownPhone;

    private Date birthday;

}
