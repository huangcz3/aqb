package com.aqb.cn.mapper;

import com.aqb.cn.bean.Photo;

public interface PhotoMapper {
    int deleteByPrimaryKey(String id);

    int insert(Photo record);

    int insertSelective(Photo record);

    Photo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Photo record);

    int updateByPrimaryKey(Photo record);
}