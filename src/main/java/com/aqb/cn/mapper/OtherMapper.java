package com.aqb.cn.mapper;

import com.aqb.cn.bean.Other;

import java.util.List;

public interface OtherMapper {
    int deleteByPrimaryKey(String id);

    int insert(Other record);

    int insertSelective(Other record);

    Other selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Other record);

    int updateByPrimaryKey(Other record);

    List<Other> queryOther();
}