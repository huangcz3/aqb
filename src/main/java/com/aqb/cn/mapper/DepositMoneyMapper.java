package com.aqb.cn.mapper;

import com.aqb.cn.bean.DepositMoney;

public interface DepositMoneyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DepositMoney record);

    int insertSelective(DepositMoney record);

    DepositMoney selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DepositMoney record);

    int updateByPrimaryKey(DepositMoney record);

    DepositMoney queryDepositMoney();
    int updateAll();//修改所有status=1的数据为0
    DepositMoney selectByStatus(Integer status);//押金金额查询
}