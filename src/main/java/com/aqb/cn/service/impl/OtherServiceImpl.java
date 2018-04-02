package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Other;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.OtherMapper;
import com.aqb.cn.service.OtherService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */
@Service
public class OtherServiceImpl implements OtherService {

    private Log logger = LogFactory.getLog(OtherServiceImpl.class);

    @Autowired
    private OtherMapper otherMapper;


    @Override
    public int add(Other other) {
        Other other_db = otherMapper.selectByPrimaryKey(other.getId());
        if(other_db == null) {
            if (otherMapper.insertSelective(other) > 0) {
                logger.debug("添加其他设置" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Other other) {
        Other other2 =otherMapper.selectByPrimaryKey(other.getId());
        if(other2 == null){
            logger.warn("尝试更新其他设置"  + " ,但是其他设置不存在");
            return Status.NOT_EXISTS;
        }
        if (otherMapper.updateByPrimaryKeySelective(other) > 0) {
            logger.debug("更新其他设置成功" + other2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Other other) {
        Other other2 = otherMapper.selectByPrimaryKey(other.getId());
        if (other2 == null) {
            logger.warn("尝试删除其他设置，但是其他设置不存在");
            return Status.NOT_EXISTS;
        }
        if (otherMapper.deleteByPrimaryKey(other.getId()) > 0 ) {
            logger.debug("删除其他设置成功" + other.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Other get(String id) {
        return otherMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<Other> queryOther() {
        return otherMapper.queryOther();
    }
}
