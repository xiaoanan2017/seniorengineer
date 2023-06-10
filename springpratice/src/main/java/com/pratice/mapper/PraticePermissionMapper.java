package com.pratice.mapper;

import com.pratice.entity.PraticePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PraticePermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PraticePermission row);

    PraticePermission selectByPrimaryKey(Long id);

    List<PraticePermission> selectAll();

    int updateByPrimaryKey(PraticePermission row);
}