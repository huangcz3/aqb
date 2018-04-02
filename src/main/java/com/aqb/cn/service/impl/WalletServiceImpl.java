package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Wallet;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.WalletMapper;
import com.aqb.cn.service.WalletService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
@Service
public class WalletServiceImpl implements WalletService {

    private Log logger = LogFactory.getLog(WalletServiceImpl.class);

    @Autowired
    private WalletMapper walletMapper;

    @Override
    public int add(Wallet wallet) {
        Wallet wallet_db = walletMapper.selectByPrimaryKey(wallet.getId());
        if (wallet_db == null){
            if (walletMapper.insertSelective(wallet) > 0){
                logger.debug("添加钱包成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Wallet wallet) {
        Wallet wallet_db = walletMapper.selectByPrimaryKey(wallet.getId());
        if (wallet_db == null){
            logger.warn("尝试更新钱包，但是钱包不存在");
            return Status.NOT_EXISTS;
        }
        if (walletMapper.updateByPrimaryKeySelective(wallet) > 0){
            logger.debug("更新钱包成功" + wallet_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Wallet wallet) {
        Wallet wallet_db = walletMapper.selectByPrimaryKey(wallet.getId());
        if (wallet_db == null) {
            logger.warn("尝试删除钱包，但是钱包不在");
            return Status.NOT_EXISTS;
        }
        if (walletMapper.deleteByPrimaryKey(wallet.getId()) > 0) {
            logger.debug("尝试删除钱包成功" + wallet_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Wallet get(String id) {
        return walletMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

    }

    @Override
    public List<Wallet> queryWallet() {
        return walletMapper.queryWallet();
    }

    @Override
    public Wallet queryByUserID(String userId) {
        return walletMapper.queryByUserID(userId);
    }
}
