package com.aqb.cn.mapper;

import com.aqb.cn.bean.Proportion;

import java.util.List;

public interface ProportionMapper {
    int deleteByPrimaryKey(String id);

    int insert(Proportion record);

    int insertSelective(Proportion record);

    Proportion selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Proportion record);

    int updateByPrimaryKey(Proportion record);

    List<Proportion> queryProportion();
    List<Proportion> selectByStatus(Integer status);
}