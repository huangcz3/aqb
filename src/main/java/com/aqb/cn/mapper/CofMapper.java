package com.aqb.cn.mapper;

import com.aqb.cn.bean.Cof;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface CofMapper {
    int deleteByPrimaryKey(String id);

    int insert(Cof record);

    int insertSelective(Cof record);

    Cof selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Cof record);

    int updateByPrimaryKey(Cof record);

    List<Cof> queryCof(QueryBase queryBase);

    List<Cof> queryCof_space(QueryBase queryBase);//朋友圈（好友空间动态）查询

    long countCof(QueryBase queryBase);

    long countCof_space(QueryBase queryBase);
}