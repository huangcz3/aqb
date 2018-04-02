package com.aqb.cn.mapper;

import com.aqb.cn.bean.ProfitPlatform;

public interface ProfitPlatformMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProfitPlatform record);

    int insertSelective(ProfitPlatform record);

    ProfitPlatform selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProfitPlatform record);

    int updateByPrimaryKey(ProfitPlatform record);

    Float sumMoneyByYear(Integer status, String year);
    Float sumMoneyByMonth(Integer status, String month);
    Float sumMoneyByDay(Integer status, String day);
}