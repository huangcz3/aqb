package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Redpacket;
import com.aqb.cn.bean.Withdrawals;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.RedpacketMapper;
import com.aqb.cn.mapper.WithdrawalsMapper;
import com.aqb.cn.service.WithdrawalsService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/1.
 */
@Service
public class WithdrawalsServiceImpl implements WithdrawalsService {

    private Log logger = LogFactory.getLog(WithdrawalsServiceImpl.class);

    @Autowired
    private WithdrawalsMapper withdrawalsMapper;
    @Autowired
    private RedpacketMapper redpacketMapper;


    @Override
    public int add(Withdrawals withdrawals) {
        Withdrawals withdrawals_db = withdrawalsMapper.selectByPrimaryKey(withdrawals.getId());
        if(withdrawals_db == null) {
            //扣除用户红包中的金额
            Float shouRu = redpacketMapper.sumSubtotal(withdrawals.getUserId(), true);//统计总收入
            Float zhiChu = redpacketMapper.sumSubtotal(withdrawals.getUserId(), false);//统计总支出
            //判断红包金额是否大于提现金额
            if(shouRu == null){
                return 20;
            }
            if(zhiChu != null){
                if((shouRu - zhiChu) < 10 || (shouRu - zhiChu) < withdrawals.getWithdrawalsMoney()){//红包大于10元才能提现
                    return 20;
                }
            }else{
                if(shouRu < 10 || shouRu < withdrawals.getWithdrawalsMoney()){
                    return 20;
                }
            }
            //红包表中添加数据
            Redpacket redpacket = new Redpacket();
            redpacket.setUserId(withdrawals.getUserId());
            redpacket.setId(UUIDCreator.getUUID());
            redpacket.setRedSubtotal(withdrawals.getWithdrawalsMoney());
            redpacket.setRedIncomeOut(false);
            redpacket.setRedCategory(1);//1--提现
            redpacket.setRedFoundDate(new Date());
            redpacket.setRedStatus(0);
            if(redpacketMapper.insertSelective(redpacket) > 0){
                if (withdrawalsMapper.insertSelective(withdrawals) > 0) {
                    logger.debug("添加提现" + "成功");
                    return Status.SUCCESS;
                }
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Withdrawals withdrawals) {
        Withdrawals withdrawals2 =withdrawalsMapper.selectByPrimaryKey(withdrawals.getId());
        if(withdrawals2 == null){
            logger.warn("尝试更新提现"  + " ,但是提现不存在");
            return Status.NOT_EXISTS;
        }
        if (withdrawalsMapper.updateByPrimaryKeySelective(withdrawals) > 0) {
            logger.debug("更新提现成功" + withdrawals2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Withdrawals withdrawals) {
        Withdrawals withdrawals2 = withdrawalsMapper.selectByPrimaryKey(withdrawals.getId());
        if (withdrawals2 == null) {
            logger.warn("尝试删除提现，但是提现不存在");
            return Status.NOT_EXISTS;
        }
        if (withdrawalsMapper.deleteByPrimaryKey(withdrawals.getId()) > 0 ) {
            logger.debug("删除提现成功" + withdrawals.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Withdrawals get(String id) {
        return withdrawalsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

        queryBase.setResults(withdrawalsMapper.queryWithdrawals(queryBase));
        queryBase.setTotalRow(withdrawalsMapper.countWithdrawals(queryBase));
    }
    
}
