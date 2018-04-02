package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Proportion;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.ProportionMapper;
import com.aqb.cn.service.ProportionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */
@Service
public class ProportionServiceImpl implements ProportionService {

    private Log logger = LogFactory.getLog(ProportionServiceImpl.class);

    @Autowired
    private ProportionMapper proportionMapper;


    @Override
    public int add(Proportion proportion) {
        Proportion proportion_db = proportionMapper.selectByPrimaryKey(proportion.getId());
        if(proportion_db == null) {
            if (proportionMapper.insertSelective(proportion) > 0) {
                logger.debug("添加兑换比例" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Proportion proportion) {
        Proportion proportion2 =proportionMapper.selectByPrimaryKey(proportion.getId());
        if(proportion2 == null){
            logger.warn("尝试更新兑换比例"  + " ,但是兑换比例不存在");
            return Status.NOT_EXISTS;
        }
        if (proportionMapper.updateByPrimaryKeySelective(proportion) > 0) {
            logger.debug("更新兑换比例成功" + proportion2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Proportion proportion) {
        Proportion proportion2 = proportionMapper.selectByPrimaryKey(proportion.getId());
        if (proportion2 == null) {
            logger.warn("尝试删除兑换比例，但是兑换比例不存在");
            return Status.NOT_EXISTS;
        }
        if (proportionMapper.deleteByPrimaryKey(proportion.getId()) > 0 ) {
            logger.debug("删除兑换比例成功" + proportion.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Proportion get(String id) {
        return proportionMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<Proportion> queryProportion() {
        return proportionMapper.queryProportion();
    }

    @Override
    public List<Proportion> selectByStatus(Integer status) {
        return proportionMapper.selectByStatus(status);
    }


}
