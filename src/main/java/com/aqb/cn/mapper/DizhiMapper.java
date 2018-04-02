package com.aqb.cn.mapper;

import com.aqb.cn.bean.Dizhi;

import java.util.List;

public interface DizhiMapper {
    int deleteByPrimaryKey(String id);

    int insert(Dizhi record);

    int insertSelective(Dizhi record);

    Dizhi selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Dizhi record);

    int updateByPrimaryKey(Dizhi record);

    List<Dizhi> queryDizhi(String userId);

    Dizhi queryByUserId(String userId);
}