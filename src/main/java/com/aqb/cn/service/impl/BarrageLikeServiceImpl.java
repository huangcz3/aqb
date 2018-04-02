package com.aqb.cn.service.impl;

import com.aqb.cn.bean.BarrageLike;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.BarrageLikeMapper;
import com.aqb.cn.service.BarrageLikeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/21/0021.
 */
@Service
public class BarrageLikeServiceImpl implements BarrageLikeService {
    private Log logger = LogFactory.getLog(BarrageLikeServiceImpl.class);

    @Autowired
    private BarrageLikeMapper barrageLikeMapper;


    @Override
    public int add(BarrageLike barrageLike) {
        BarrageLike barrageLike_db = barrageLikeMapper.selectByPrimaryKey(barrageLike.getId());
        if(barrageLike_db == null) {
            if (barrageLikeMapper.insertSelective(barrageLike) > 0) {
                logger.debug("添加点赞" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(BarrageLike barrageLike) {
        BarrageLike barrageLike2 =barrageLikeMapper.selectByPrimaryKey(barrageLike.getId());
        if(barrageLike2 == null){
            logger.warn("尝试更新点赞"  + " ,但是点赞不存在");
            return Status.NOT_EXISTS;
        }
        if (barrageLikeMapper.updateByPrimaryKeySelective(barrageLike) > 0) {
            logger.debug("更新点赞成功" + barrageLike2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(BarrageLike barrageLike) {
        BarrageLike barrageLike2 = barrageLikeMapper.selectByPrimaryKey(barrageLike.getId());
        if (barrageLike2 == null) {
            logger.warn("尝试删除点赞，但是点赞不存在");
            return Status.NOT_EXISTS;
        }
        if (barrageLikeMapper.deleteByPrimaryKey(barrageLike.getId()) > 0 ) {
            logger.debug("删除点赞成功" + barrageLike.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public BarrageLike get(String id) {
        return barrageLikeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

        queryBase.setResults(barrageLikeMapper.queryBarrageLike(queryBase));
        queryBase.setTotalRow(barrageLikeMapper.countBarrageLike(queryBase));
    }

    @Override
    public BarrageLike queryByUserIdBarrageId(String userId, String barrageId) {
        return barrageLikeMapper.queryByUserIdBarrageId(userId,barrageId);
    }
}
