package com.pratice.mapper;

import com.pratice.entity.PraticeUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PraticeUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PraticeUser row);

    PraticeUser selectByPrimaryKey(Long id);

    List<PraticeUser> selectAll();

    int updateByPrimaryKey(PraticeUser row);

    PraticeUser loadUserByUsername(String userName);
}