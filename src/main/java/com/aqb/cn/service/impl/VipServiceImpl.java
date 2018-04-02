package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Vip;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.VipMapper;
import com.aqb.cn.service.VipService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
@Service
public class VipServiceImpl implements VipService {

    private Log logger = LogFactory.getLog(VipServiceImpl.class);

    @Autowired
    private VipMapper vipMapper;


    @Override
    public int add(Vip vip) {
        Vip vip_db = vipMapper.selectByPrimaryKey(vip.getId());
        if(vip_db == null) {
            if (vipMapper.insertSelective(vip) > 0) {
                logger.debug("添加vip权限" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Vip vip) {
        Vip vip2 =vipMapper.selectByPrimaryKey(vip.getId());
        if(vip2 == null){
            logger.warn("尝试更新vip权限"  + " ,但是vip权限不存在");
            return Status.NOT_EXISTS;
        }
        if (vipMapper.updateByPrimaryKeySelective(vip) > 0) {
            logger.debug("更新vip权限成功" + vip2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Vip vip) {
        Vip vip2 = vipMapper.selectByPrimaryKey(vip.getId());
        if (vip2 == null) {
            logger.warn("尝试删除vip权限，但是vip权限不存在");
            return Status.NOT_EXISTS;
        }
        if (vipMapper.deleteByPrimaryKey(vip.getId()) > 0 ) {
            logger.debug("删除vip权限成功" + vip.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Vip get(String id) {
        return vipMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<Vip> queryvip() {
        return vipMapper.queryVip();
    }

    @Override
    public Vip selectByGrade(Integer vipGrade) {
        return vipMapper.selectByGrade(vipGrade);
    }

}
