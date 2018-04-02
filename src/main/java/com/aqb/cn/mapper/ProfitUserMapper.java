package com.aqb.cn.mapper;

import com.aqb.cn.bean.ProfitUser;

public interface ProfitUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProfitUser record);

    int insertSelective(ProfitUser record);

    ProfitUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProfitUser record);

    int updateByPrimaryKey(ProfitUser record);
}