package com.pratice.mapper;

import com.pratice.entity.PraticeRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PraticeRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PraticeRole row);

    PraticeRole selectByPrimaryKey(Long id);

    List<PraticeRole> selectAll();

    int updateByPrimaryKey(PraticeRole row);

    List<PraticeRole> getRolesByUserId(Long id);
    
}