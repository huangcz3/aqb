package com.aqb.cn.mapper;

import com.aqb.cn.bean.Daxiaoquan;

import java.util.List;

public interface DaxiaoquanMapper {
    int deleteByPrimaryKey(String id);

    int insert(Daxiaoquan record);

    int insertSelective(Daxiaoquan record);

    Daxiaoquan selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Daxiaoquan record);

    int updateByPrimaryKey(Daxiaoquan record);

    List<Daxiaoquan> queryDaxiaoquan(Integer status);
    List<Daxiaoquan> queryDaxiaoquanStatus(Integer status, Integer xulie);

    Daxiaoquan selectByCanshu(Integer status, Integer xulie, Float daxiaoquanCanshu);
}