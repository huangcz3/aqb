package com.aqb.cn.service.impl;

import com.aqb.cn.bean.BarrageUnlike;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.BarrageUnlikeMapper;
import com.aqb.cn.service.BarrageUnlikeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/21/0021.
 */
@Service
public class BarrageUnlikeServiceImpl implements BarrageUnlikeService {
    private Log logger = LogFactory.getLog(BarrageUnlikeServiceImpl.class);

    @Autowired
    private BarrageUnlikeMapper barrageUnlikeMapper;


    @Override
    public int add(BarrageUnlike barrageUnlike) {
        BarrageUnlike barrageUnlike_db = barrageUnlikeMapper.selectByPrimaryKey(barrageUnlike.getId());
        if(barrageUnlike_db == null) {
            if (barrageUnlikeMapper.insertSelective(barrageUnlike) > 0) {
                logger.debug("添加点赞" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(BarrageUnlike barrageUnlike) {
        BarrageUnlike barrageUnlike2 =barrageUnlikeMapper.selectByPrimaryKey(barrageUnlike.getId());
        if(barrageUnlike2 == null){
            logger.warn("尝试更新点赞"  + " ,但是点赞不存在");
            return Status.NOT_EXISTS;
        }
        if (barrageUnlikeMapper.updateByPrimaryKeySelective(barrageUnlike) > 0) {
            logger.debug("更新点赞成功" + barrageUnlike2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(BarrageUnlike barrageUnlike) {
        BarrageUnlike barrageUnlike2 = barrageUnlikeMapper.selectByPrimaryKey(barrageUnlike.getId());
        if (barrageUnlike2 == null) {
            logger.warn("尝试删除点赞，但是点赞不存在");
            return Status.NOT_EXISTS;
        }
        if (barrageUnlikeMapper.deleteByPrimaryKey(barrageUnlike.getId()) > 0 ) {
            logger.debug("删除点赞成功" + barrageUnlike.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public BarrageUnlike get(String id) {
        return barrageUnlikeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

        queryBase.setResults(barrageUnlikeMapper.queryBarrageUnlike(queryBase));
        queryBase.setTotalRow(barrageUnlikeMapper.countBarrageUnlike(queryBase));
    }

    @Override
    public BarrageUnlike queryByUserIdBarrageId(String userId, String barrageId) {
        return barrageUnlikeMapper.queryByUserIdBarrageId(userId,barrageId);
    }
}
