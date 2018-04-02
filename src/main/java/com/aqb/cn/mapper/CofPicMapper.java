package com.aqb.cn.mapper;

import com.aqb.cn.bean.CofPic;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface CofPicMapper {
    int deleteByPrimaryKey(String id);

    int insert(CofPic record);

    int insertSelective(CofPic record);

    CofPic selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CofPic record);

    int updateByPrimaryKey(CofPic record);

    List<CofPic> queryCofPic(QueryBase queryBase);

    long countCofPic(QueryBase queryBase);

    List<CofPic> selectByCofId(String cofId);
}