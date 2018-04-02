package com.aqb.cn.mapper;

import com.aqb.cn.bean.AdverDate;

import java.util.List;

public interface AdverDateMapper {
    int deleteByPrimaryKey(String id);

    int insert(AdverDate record);

    int insertSelective(AdverDate record);

    AdverDate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AdverDate record);

    int updateByPrimaryKey(AdverDate record);

    List<AdverDate> selectByAdverId(String advertisementId);
}