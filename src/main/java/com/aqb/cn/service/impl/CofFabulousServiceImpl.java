package com.aqb.cn.service.impl;

import com.aqb.cn.bean.CofFabulous;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.CofFabulousMapper;
import com.aqb.cn.service.CofFabulousService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
@Service
public class CofFabulousServiceImpl implements CofFabulousService{
    private Log logger = LogFactory.getLog(CofFabulousServiceImpl.class);

    @Autowired
    private CofFabulousMapper cofFabulousMapper;


    @Override
    public int add(CofFabulous cofFabulous) {
        CofFabulous cofFabulous_db = cofFabulousMapper.selectByPrimaryKey(cofFabulous.getId());
        if(cofFabulous_db == null) {
            if (cofFabulousMapper.insertSelective(cofFabulous) > 0) {
                logger.debug("添加点赞" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(CofFabulous cofFabulous) {
        CofFabulous cofFabulous2 =cofFabulousMapper.selectByPrimaryKey(cofFabulous.getId());
        if(cofFabulous2 == null){
            logger.warn("尝试更新点赞"  + " ,但是点赞不存在");
            return Status.NOT_EXISTS;
        }
        if (cofFabulousMapper.updateByPrimaryKeySelective(cofFabulous) > 0) {
            logger.debug("更新点赞成功" + cofFabulous2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(CofFabulous cofFabulous) {
        CofFabulous cofFabulous2 = cofFabulousMapper.selectByPrimaryKey(cofFabulous.getId());
        if (cofFabulous2 == null) {
            logger.warn("尝试删除点赞，但是点赞不存在");
            return Status.NOT_EXISTS;
        }
        if (cofFabulousMapper.deleteByPrimaryKey(cofFabulous.getId()) > 0 ) {
            logger.debug("删除点赞成功" + cofFabulous.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public CofFabulous get(String id) {
        return cofFabulousMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

        queryBase.setResults(cofFabulousMapper.queryCofFabulous(queryBase));
        queryBase.setTotalRow(cofFabulousMapper.countCofFabulous(queryBase));
    }

    @Override
    public CofFabulous queryByUserIdCofId(String userId, String cofId) {
        return cofFabulousMapper.queryByUserIdCofId(userId,cofId);
    }
}
