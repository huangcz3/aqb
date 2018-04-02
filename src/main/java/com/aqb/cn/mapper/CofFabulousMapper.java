package com.aqb.cn.mapper;

import com.aqb.cn.bean.CofFabulous;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface CofFabulousMapper {
    int deleteByPrimaryKey(String id);

    int insert(CofFabulous record);

    int insertSelective(CofFabulous record);

    CofFabulous selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CofFabulous record);

    int updateByPrimaryKey(CofFabulous record);

    List<CofFabulous> queryCofFabulous(QueryBase queryBase);

    long countCofFabulous(QueryBase queryBase);

    List<CofFabulous> selectByUserId(String userId);

    List<CofFabulous> selectByCofId(String cofId);

    CofFabulous queryByUserIdCofId(String userId,String cofId);
}