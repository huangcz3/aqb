package com.aqb.cn.service;

import com.aqb.cn.bean.DepositMoney;

/**
 * Created by Administrator on 2017/7/28/0028.
 */
public interface DepositMoneyService extends BaseService<DepositMoney>{
    DepositMoney queryDepositMoney();//查询当前实行的押金金额
}
