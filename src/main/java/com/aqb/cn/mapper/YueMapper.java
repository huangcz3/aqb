package com.aqb.cn.mapper;

import com.aqb.cn.bean.Yue;

import java.util.List;

public interface YueMapper {
    int deleteByPrimaryKey(String id);

    int insert(Yue record);

    int insertSelective(Yue record);

    Yue selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Yue record);

    int updateByPrimaryKey(Yue record);

    List<Yue> queryYue();

    List<Yue> queryYueByUserId(String userId);

}