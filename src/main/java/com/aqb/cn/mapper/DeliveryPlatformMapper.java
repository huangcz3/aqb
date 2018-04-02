package com.aqb.cn.mapper;

import com.aqb.cn.bean.DeliveryPlatform;

public interface DeliveryPlatformMapper {
    int deleteByPrimaryKey(String id);

    int insert(DeliveryPlatform record);

    int insertSelective(DeliveryPlatform record);

    DeliveryPlatform selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DeliveryPlatform record);

    int updateByPrimaryKey(DeliveryPlatform record);
}