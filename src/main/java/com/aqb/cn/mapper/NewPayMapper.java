package com.aqb.cn.mapper;

import com.aqb.cn.bean.NewPay;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface NewPayMapper {
    int deleteByPrimaryKey(String id);

    int insert(NewPay record);

    int insertSelective(NewPay record);

    NewPay selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NewPay record);

    int updateByPrimaryKey(NewPay record);

    List<NewPay> queryNewPay(QueryBase queryBase);
    long countNewPay(QueryBase queryBase);

    List<NewPay> queryNewPayStatus(String userId);//查询用户是否有未读消息
    int updateByStatus(String userId);
}