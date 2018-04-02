package com.aqb.cn.mapper;

import com.aqb.cn.bean.Deposit;

import java.util.List;

public interface DepositMapper {
    int deleteByPrimaryKey(String id);

    int insert(Deposit record);

    int insertSelective(Deposit record);

    Deposit selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Deposit record);

    int updateByPrimaryKey(Deposit record);

    Deposit queryDepositByUserId(String userId);

    List<Deposit> selectByUserId(String userId);
}