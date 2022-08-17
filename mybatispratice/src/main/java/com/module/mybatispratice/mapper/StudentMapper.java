package com.module.mybatispratice.mapper;

import com.module.mybatispratice.po.StudentDO;
import com.module.mybatispratice.po.StudentEO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaoaa
 * @date 2022/7/27 22:49
 **/
@Mapper
public interface StudentMapper {

    Long insert(@Param("studentDO") StudentDO studentDO);

    StudentDO select(Long id);

    List<StudentDO> list(Long id);

    /**
     * 1 对 1 映射 EO -> scoreDO
     * @param id
     * @return
     */
    StudentEO selectEO(Long id);

    /**
     * 1 对 多 映射 EO -> list
     * @param id
     * @return
     */
    StudentEO selectEOlist(Long id);

    Long updateStudentById(@Param("studentDO") StudentDO studentDO);
}
