package com.aqb.cn.mapper;

import com.aqb.cn.bean.AdverPlatform;

public interface AdverPlatformMapper {
    int deleteByPrimaryKey(String id);

    int insert(AdverPlatform record);

    int insertSelective(AdverPlatform record);

    AdverPlatform selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AdverPlatform record);

    int updateByPrimaryKey(AdverPlatform record);
}