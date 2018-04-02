package com.aqb.cn.mapper;

import com.aqb.cn.bean.PopAdv;

import java.util.List;

public interface PopAdvMapper {
    int deleteByPrimaryKey(String id);

    int insert(PopAdv record);

    int insertSelective(PopAdv record);

    PopAdv selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PopAdv record);

    int updateByPrimaryKey(PopAdv record);

    List<PopAdv> queryPopAdv();

    long countPopAdv();
}