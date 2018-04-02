package com.aqb.cn.mapper;

import com.aqb.cn.bean.Parameter;

public interface ParameterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Parameter record);

    int insertSelective(Parameter record);

    Parameter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Parameter record);

    int updateByPrimaryKey(Parameter record);

    Parameter queryPopPara();
}