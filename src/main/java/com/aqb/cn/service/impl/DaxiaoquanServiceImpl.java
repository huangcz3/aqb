package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Daxiaoquan;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.DaxiaoquanMapper;
import com.aqb.cn.service.DaxiaoquanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
@Service
public class DaxiaoquanServiceImpl implements DaxiaoquanService {

    private Log logger = LogFactory.getLog(DaxiaoquanServiceImpl.class);

    @Autowired
    private DaxiaoquanMapper daxiaoquanMapper;


    @Override
    public int add(Daxiaoquan daxiaoquan) {
        Daxiaoquan daxiaoquan_db = daxiaoquanMapper.selectByCanshu(daxiaoquan.getStatus(),daxiaoquan.getXulie(), daxiaoquan.getDaxiaoquanCanshu());
        if(daxiaoquan_db == null) {
            if (daxiaoquanMapper.insertSelective(daxiaoquan) > 0) {
                logger.debug("添加大小圈设置" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Daxiaoquan daxiaoquan) {
        Daxiaoquan daxiaoquan2 =daxiaoquanMapper.selectByPrimaryKey(daxiaoquan.getId());
        if(daxiaoquan2 == null){
            logger.warn("尝试更新大小圈设置"  + " ,但是大小圈设置不存在");
            return Status.NOT_EXISTS;
        }
        if (daxiaoquanMapper.updateByPrimaryKeySelective(daxiaoquan) > 0) {
            logger.debug("更新大小圈设置成功" + daxiaoquan2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Daxiaoquan daxiaoquan) {
        Daxiaoquan daxiaoquan2 = daxiaoquanMapper.selectByPrimaryKey(daxiaoquan.getId());
        if (daxiaoquan2 == null) {
            logger.warn("尝试删除大小圈设置，但是大小圈设置不存在");
            return Status.NOT_EXISTS;
        }
        if (daxiaoquanMapper.deleteByPrimaryKey(daxiaoquan.getId()) > 0 ) {
            logger.debug("删除大小圈设置成功" + daxiaoquan.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Daxiaoquan get(String id) {
        return daxiaoquanMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<Daxiaoquan> queryDaxiaoquan(Integer status) {
        return daxiaoquanMapper.queryDaxiaoquan(status);
    }

    @Override
    public List<Daxiaoquan> queryDaxiaoquanStatus(Integer status, Integer xulie) {
        return daxiaoquanMapper.queryDaxiaoquanStatus(status,xulie);
    }

}
