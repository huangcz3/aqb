package com.aqb.cn.mapper;

import com.aqb.cn.bean.RechargeOrder;

public interface RechargeOrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(RechargeOrder record);

    int insertSelective(RechargeOrder record);

    RechargeOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RechargeOrder record);

    int updateByPrimaryKey(RechargeOrder record);
}