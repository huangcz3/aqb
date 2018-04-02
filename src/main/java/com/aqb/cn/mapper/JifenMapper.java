package com.aqb.cn.mapper;

import com.aqb.cn.bean.Jifen;

import java.util.List;

public interface JifenMapper {
    int deleteByPrimaryKey(String id);

    int insert(Jifen record);

    int insertSelective(Jifen record);

    Jifen selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Jifen record);

    int updateByPrimaryKey(Jifen record);

    List<Jifen> queryJifen();

    List<Jifen> queryJifenByUserId(String userId);
}