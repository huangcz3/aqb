package com.aqb.cn.mapper;

import com.aqb.cn.bean.Sign;

import java.util.Date;
import java.util.List;

public interface SignMapper {
    int deleteByPrimaryKey(String id);

    int insert(Sign record);

    int insertSelective(Sign record);

    Sign selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Sign record);

    int updateByPrimaryKey(Sign record);

    List<Sign> querySign();

    List<Sign> querySignByUserId(String userId);
    List<Sign> querySevenSign(String userId,Date dateSeven);
    Sign querytodaySign(String userId,Date date);//当前时间
}