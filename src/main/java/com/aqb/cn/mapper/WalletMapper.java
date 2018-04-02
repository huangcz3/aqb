package com.aqb.cn.mapper;

import com.aqb.cn.bean.Wallet;

import java.util.List;

public interface WalletMapper {
    int deleteByPrimaryKey(String id);

    int insert(Wallet record);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Wallet record);

    int updateByPrimaryKey(Wallet record);

    List<Wallet> queryWallet();

    Wallet queryByUserID(String userId);
}