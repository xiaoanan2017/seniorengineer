package com.pratice.mapper;

import com.pratice.entity.PraticeUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PraticeUserRoleMapper {
    int insert(PraticeUserRole row);

    List<PraticeUserRole> selectAll();
}