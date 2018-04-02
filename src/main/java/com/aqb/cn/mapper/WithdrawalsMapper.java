package com.aqb.cn.mapper;

import com.aqb.cn.bean.Withdrawals;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface WithdrawalsMapper {
    int deleteByPrimaryKey(String id);

    int insert(Withdrawals record);

    int insertSelective(Withdrawals record);

    Withdrawals selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Withdrawals record);

    int updateByPrimaryKey(Withdrawals record);

    List<Withdrawals> queryWithdrawals(QueryBase queryBase);
    long countWithdrawals(QueryBase queryBase);
}