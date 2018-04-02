package com.aqb.cn.mapper;

import com.aqb.cn.bean.BarrageDeleteTime;

import java.util.List;

public interface BarrageDeleteTimeMapper {
    int deleteByPrimaryKey(String id);

    int insert(BarrageDeleteTime record);

    int insertSelective(BarrageDeleteTime record);

    BarrageDeleteTime selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BarrageDeleteTime record);

    int updateByPrimaryKey(BarrageDeleteTime record);


    BarrageDeleteTime selectInUse();//查询正在启用的弹幕定时删除时间

    List<BarrageDeleteTime> queryBarrageDeleteTime();//查询弹幕定时删除时间
}