package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Deposit;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.DepositMapper;
import com.aqb.cn.service.DepositService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/27/0027.
 */
@Service
public class DepositServiceImpl implements DepositService {
    private Log logger = LogFactory.getLog(DepositServiceImpl.class);

    @Autowired
    private DepositMapper depositMapper;


    @Override
    public int add(Deposit deposit) {
        Deposit deposit_db = depositMapper.selectByPrimaryKey(deposit.getId());
        if(deposit_db == null) {
            if (depositMapper.insertSelective(deposit) > 0) {
                logger.debug("添加deposit权限" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Deposit deposit) {
        Deposit deposit2 =depositMapper.selectByPrimaryKey(deposit.getId());
        if(deposit2 == null){
            logger.warn("尝试更新deposit权限"  + " ,但是deposit权限不存在");
            return Status.NOT_EXISTS;
        }
        if (depositMapper.updateByPrimaryKeySelective(deposit) > 0) {
            logger.debug("更新deposit权限成功" + deposit2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Deposit deposit) {
        Deposit deposit2 = depositMapper.selectByPrimaryKey(deposit.getId());
        if (deposit2 == null) {
            logger.warn("尝试删除deposit权限，但是deposit权限不存在");
            return Status.NOT_EXISTS;
        }
        if (depositMapper.deleteByPrimaryKey(deposit.getId()) > 0 ) {
            logger.debug("删除deposit权限成功" + deposit.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Deposit get(String id) {
        return depositMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public Deposit queryDepositByUserId(String userId) {
        return depositMapper.queryDepositByUserId(userId);
    }
}
