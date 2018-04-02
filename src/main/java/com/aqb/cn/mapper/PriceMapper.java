package com.aqb.cn.mapper;

import com.aqb.cn.bean.Price;

import java.util.List;

public interface PriceMapper {
    int deleteByPrimaryKey(String id);

    int insert(Price record);

    int insertSelective(Price record);

    Price selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Price record);

    int updateByPrimaryKey(Price record);

    List<Price> queryPrice(String priceCity);
    Price selectJiage();
}