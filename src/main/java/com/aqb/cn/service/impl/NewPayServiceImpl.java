package com.aqb.cn.service.impl;

import com.aqb.cn.bean.NewPay;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.NewPayMapper;
import com.aqb.cn.service.NewPayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */
@Service
public class NewPayServiceImpl implements NewPayService {

    private Log logger = LogFactory.getLog(NewPayServiceImpl.class);

    @Autowired
    private NewPayMapper newPayMapper;


    @Override
    public int add(NewPay newPay) {
        NewPay newPay_db = newPayMapper.selectByPrimaryKey(newPay.getId());
        if(newPay_db == null) {
            if (newPayMapper.insertSelective(newPay) > 0) {
                logger.debug("添加支付消息" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(NewPay newPay) {
        NewPay newPay2 =newPayMapper.selectByPrimaryKey(newPay.getId());
        if(newPay2 == null){
            logger.warn("尝试更新支付消息"  + " ,但是支付消息不存在");
            return Status.NOT_EXISTS;
        }
        if (newPayMapper.updateByPrimaryKeySelective(newPay) > 0) {
            logger.debug("更新支付消息成功" + newPay2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(NewPay newPay) {
        NewPay newPay2 = newPayMapper.selectByPrimaryKey(newPay.getId());
        if (newPay2 == null) {
            logger.warn("尝试删除支付消息，但是支付消息不存在");
            return Status.NOT_EXISTS;
        }
        if (newPayMapper.deleteByPrimaryKey(newPay.getId()) > 0 ) {
            logger.debug("删除支付消息成功" + newPay.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public NewPay get(String id) {
        return newPayMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(newPayMapper.queryNewPay(queryBase));
        queryBase.setTotalRow(newPayMapper.countNewPay(queryBase));
    }

    @Override
    public boolean queryNewPayStatus(String userId) {
        List<NewPay> newPays = newPayMapper.queryNewPayStatus(userId);
        if(newPays == null || newPays.size() == 0){
            return false;//没有查到未读支付消息，返回false
        }
        return true;//否则返回true
    }

    @Override
    public int updateByStatus(String userId) {
        return newPayMapper.updateByStatus(userId);
    }

}
