package com.aqb.cn.service;

import com.aqb.cn.bean.Deposit;

/**
 * Created by Administrator on 2017/7/27/0027.
 */
public interface DepositService extends BaseService<Deposit>{
    Deposit queryDepositByUserId(String userId);
}
