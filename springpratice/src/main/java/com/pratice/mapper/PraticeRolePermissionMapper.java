package com.pratice.mapper;

import com.pratice.entity.PraticeRolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PraticeRolePermissionMapper {
    int insert(PraticeRolePermission row);

    List<PraticeRolePermission> selectAll();
}