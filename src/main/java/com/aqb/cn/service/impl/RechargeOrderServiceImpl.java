package com.aqb.cn.service.impl;

import com.aqb.cn.bean.RechargeOrder;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.RechargeOrderMapper;
import com.aqb.cn.service.RechargeOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
@Service
public class RechargeOrderServiceImpl implements RechargeOrderService {

    private Log logger = LogFactory.getLog(RechargeOrderServiceImpl.class);

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;


    @Override
    public int add(RechargeOrder rechargeOrder) {
        RechargeOrder rechargeOrder_db = rechargeOrderMapper.selectByPrimaryKey(rechargeOrder.getId());
        if(rechargeOrder_db == null) {
            if (rechargeOrderMapper.insertSelective(rechargeOrder) > 0) {
                logger.debug("添加充值订单" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(RechargeOrder rechargeOrder) {
        RechargeOrder rechargeOrder2 =rechargeOrderMapper.selectByPrimaryKey(rechargeOrder.getId());
        if(rechargeOrder2 == null){
            logger.warn("尝试更新充值订单"  + " ,但是充值订单不存在");
            return Status.NOT_EXISTS;
        }
        if (rechargeOrderMapper.updateByPrimaryKeySelective(rechargeOrder) > 0) {
            logger.debug("更新充值订单成功" + rechargeOrder2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(RechargeOrder rechargeOrder) {
        RechargeOrder rechargeOrder2 = rechargeOrderMapper.selectByPrimaryKey(rechargeOrder.getId());
        if (rechargeOrder2 == null) {
            logger.warn("尝试删除充值订单，但是充值订单不存在");
            return Status.NOT_EXISTS;
        }
        if (rechargeOrderMapper.deleteByPrimaryKey(rechargeOrder.getId()) > 0 ) {
            logger.debug("删除充值订单成功" + rechargeOrder.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public RechargeOrder get(String id) {
        return rechargeOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    
}
