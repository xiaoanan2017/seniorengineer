package com.module.mybatispratice.po;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaoaa
 * @date 2022/7/27 22:57
 **/
@Table(name =  "student")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDO implements Serializable {

    private static final long serialVersionUID = 1071273986889182372L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    private String address;

    private String ownPhone;

    private Date birthday;

}
