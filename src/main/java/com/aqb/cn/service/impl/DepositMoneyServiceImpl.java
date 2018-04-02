package com.aqb.cn.service.impl;

import com.aqb.cn.bean.DepositMoney;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.DepositMoneyMapper;
import com.aqb.cn.service.DepositMoneyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/28/0028.
 */
@Service
public class DepositMoneyServiceImpl implements DepositMoneyService {

    private Log logger = LogFactory.getLog(DepositMoneyServiceImpl.class);

    @Autowired
    private DepositMoneyMapper depositMoneyMapper;


    @Override
    public int add(DepositMoney depositMoney) {
        DepositMoney depositMoney_db = depositMoneyMapper.selectByPrimaryKey(depositMoney.getId());
        if(depositMoney_db == null) {
            if (depositMoneyMapper.insertSelective(depositMoney) > 0) {
                logger.debug("添加depositMoney权限" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(DepositMoney depositMoney) {
        DepositMoney depositMoney2 =depositMoneyMapper.selectByPrimaryKey(depositMoney.getId());
        if(depositMoney2 == null){
            logger.warn("尝试更新depositMoney权限"  + " ,但是depositMoney权限不存在");
            return Status.NOT_EXISTS;
        }
        if (depositMoneyMapper.updateByPrimaryKeySelective(depositMoney) > 0) {
            logger.debug("更新depositMoney权限成功" + depositMoney2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(DepositMoney depositMoney) {
        DepositMoney depositMoney2 = depositMoneyMapper.selectByPrimaryKey(depositMoney.getId());
        if (depositMoney2 == null) {
            logger.warn("尝试删除depositMoney权限，但是depositMoney权限不存在");
            return Status.NOT_EXISTS;
        }
        if (depositMoneyMapper.deleteByPrimaryKey(depositMoney.getId()) > 0 ) {
            logger.debug("删除depositMoney权限成功" + depositMoney.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public DepositMoney get(String id) {
//        return depositMoneyMapper.selectByPrimaryKey(Integer.parseInt(id));
        return null;
    }

    @Override
    public void query(QueryBase queryBase) {
    }


    @Override
    public DepositMoney queryDepositMoney() {
        return depositMoneyMapper.queryDepositMoney();
    }
}
