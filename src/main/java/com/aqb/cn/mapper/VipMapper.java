package com.aqb.cn.mapper;

import com.aqb.cn.bean.Vip;

import java.util.List;

public interface VipMapper {
    int deleteByPrimaryKey(String id);

    int insert(Vip record);

    int insertSelective(Vip record);

    Vip selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Vip record);

    int updateByPrimaryKey(Vip record);
    List<Vip> queryVip();

    Vip selectByGrade(Integer vipGrade);
}