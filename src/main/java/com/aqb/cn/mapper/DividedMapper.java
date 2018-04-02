package com.aqb.cn.mapper;

import com.aqb.cn.bean.Divided;

public interface DividedMapper {
    int deleteByPrimaryKey(String id);

    int insert(Divided record);

    int insertSelective(Divided record);

    Divided selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Divided record);

    int updateByPrimaryKey(Divided record);

    Divided queryDivided();
}