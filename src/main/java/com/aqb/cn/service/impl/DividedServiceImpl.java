package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Divided;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.DividedMapper;
import com.aqb.cn.service.DividedService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/11.
 */
@Service
public class DividedServiceImpl implements DividedService {

    private Log logger = LogFactory.getLog(DividedServiceImpl.class);

    @Autowired
    private DividedMapper dividedMapper;


    @Override
    public int add(Divided divided) {
        Divided divided_db = dividedMapper.selectByPrimaryKey(divided.getId());
        if(divided_db == null) {
            if (dividedMapper.insertSelective(divided) > 0) {
                logger.debug("添加车主分成比例" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Divided divided) {
        divided.setId("KDSJHFIO8943HIUJHDSF9978H9HHSUIH");
        if (dividedMapper.updateByPrimaryKeySelective(divided) > 0) {
            logger.debug("更新车主分成比例成功" + divided.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Divided divided) {
        Divided divided2 = dividedMapper.selectByPrimaryKey(divided.getId());
        if (divided2 == null) {
            logger.warn("尝试删除车主分成比例，但是车主分成比例不存在");
            return Status.NOT_EXISTS;
        }
        if (dividedMapper.deleteByPrimaryKey(divided.getId()) > 0 ) {
            logger.debug("删除车主分成比例成功" + divided.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Divided get(String id) {
        return dividedMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public Divided queryDivided() {
        return dividedMapper.queryDivided();
    }
}
