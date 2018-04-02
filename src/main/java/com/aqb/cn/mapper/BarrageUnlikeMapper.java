package com.aqb.cn.mapper;

import com.aqb.cn.bean.BarrageUnlike;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface BarrageUnlikeMapper {
    int deleteByPrimaryKey(String id);

    int insert(BarrageUnlike record);

    int insertSelective(BarrageUnlike record);

    BarrageUnlike selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BarrageUnlike record);

    int updateByPrimaryKey(BarrageUnlike record);

    BarrageUnlike queryByUserIdBarrageId(String userId,String barrageId);

    List<BarrageUnlike> queryBarrageUnlike(QueryBase queryBase);

    long countBarrageUnlike(QueryBase queryBase);
}