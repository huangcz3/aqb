package com.aqb.cn.service;

import com.aqb.cn.bean.Wallet;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public interface WalletService extends BaseService<Wallet>{
    List<Wallet> queryWallet();

    Wallet queryByUserID(String userId);
}
