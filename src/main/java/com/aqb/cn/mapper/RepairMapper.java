package com.aqb.cn.mapper;

import com.aqb.cn.bean.Repair;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface RepairMapper {
    int deleteByPrimaryKey(String id);

    int insert(Repair record);

    int insertSelective(Repair record);

    Repair selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Repair record);

    int updateByPrimaryKey(Repair record);

    List<Repair> queryRepair1(QueryBase queryBase);//查全部
    List<Repair> queryRepair2(QueryBase queryBase);//差修车
    List<Repair> queryRepair3(QueryBase queryBase);//查
    List<Repair> queryRepair4(QueryBase queryBase);

    long countRepair1(QueryBase queryBase);
    long countRepair2(QueryBase queryBase);
    long countRepair3(QueryBase queryBase);
    long countRepair4(QueryBase queryBase);

    List<Repair> queryShop(QueryBase queryBase);

    Long countShop(QueryBase queryBase);

    Repair queryById(String id);
}