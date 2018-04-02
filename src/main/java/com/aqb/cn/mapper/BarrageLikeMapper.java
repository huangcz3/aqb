package com.aqb.cn.mapper;

import com.aqb.cn.bean.BarrageLike;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface BarrageLikeMapper {

    int deleteByPrimaryKey(String id);

    int insert(BarrageLike record);

    int insertSelective(BarrageLike record);

    BarrageLike selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BarrageLike record);

    int updateByPrimaryKey(BarrageLike record);

    BarrageLike queryByUserIdBarrageId(String userId,String barrageId);

    List<BarrageLike> queryBarrageLike(QueryBase queryBase);

    long countBarrageLike(QueryBase queryBase);


}