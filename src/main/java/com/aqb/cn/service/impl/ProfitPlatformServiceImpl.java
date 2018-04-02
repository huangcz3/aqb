package com.aqb.cn.service.impl;

import com.aqb.cn.bean.ProfitPlatform;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.ProfitPlatformMapper;
import com.aqb.cn.service.ProfitPlatformService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */
@Service
public class ProfitPlatformServiceImpl implements ProfitPlatformService {

    private Log logger = LogFactory.getLog(ProfitPlatformServiceImpl.class);

    @Autowired
    private ProfitPlatformMapper profitPlatformMapper;


    @Override
    public int add(ProfitPlatform profitPlatform) {
        ProfitPlatform profitPlatform_db = profitPlatformMapper.selectByPrimaryKey(profitPlatform.getId());
        if(profitPlatform_db == null) {
            if (profitPlatformMapper.insertSelective(profitPlatform) > 0) {
                logger.debug("添加平台收益" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(ProfitPlatform profitPlatform) {
        ProfitPlatform profitPlatform2 =profitPlatformMapper.selectByPrimaryKey(profitPlatform.getId());
        if(profitPlatform2 == null){
            logger.warn("尝试更新平台收益"  + " ,但是平台收益不存在");
            return Status.NOT_EXISTS;
        }
        if (profitPlatformMapper.updateByPrimaryKeySelective(profitPlatform) > 0) {
            logger.debug("更新平台收益成功" + profitPlatform2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(ProfitPlatform profitPlatform) {
        ProfitPlatform profitPlatform2 = profitPlatformMapper.selectByPrimaryKey(profitPlatform.getId());
        if (profitPlatform2 == null) {
            logger.warn("尝试删除平台收益，但是平台收益不存在");
            return Status.NOT_EXISTS;
        }
        if (profitPlatformMapper.deleteByPrimaryKey(profitPlatform.getId()) > 0 ) {
            logger.debug("删除平台收益成功" + profitPlatform.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public ProfitPlatform get(String id) {
        return profitPlatformMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }


    @Override
    public Float sumMoneyByYear(Integer status, String year) {
        return profitPlatformMapper.sumMoneyByYear(status, year);
    }

    @Override
    public Float sumMoneyByMonth(Integer status, String month) {
        return profitPlatformMapper.sumMoneyByMonth(status, month);
    }

    @Override
    public Float sumMoneyByDay(Integer status, String day) {
        return profitPlatformMapper.sumMoneyByDay(status, day);
    }
}
