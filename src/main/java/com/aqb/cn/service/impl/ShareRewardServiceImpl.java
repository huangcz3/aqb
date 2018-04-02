package com.aqb.cn.service.impl;

import com.aqb.cn.bean.ShareReward;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.ShareRewardMapper;
import com.aqb.cn.service.ShareRewardService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */
@Service
public class ShareRewardServiceImpl implements ShareRewardService {

    private Log logger = LogFactory.getLog(ShareRewardServiceImpl.class);

    @Autowired
    private ShareRewardMapper shareRewardMapper;


    @Override
    public int add(ShareReward shareReward) {
        ShareReward shareReward_db = shareRewardMapper.selectByPrimaryKey(shareReward.getId());
        if(shareReward_db == null) {
            if (shareRewardMapper.insertSelective(shareReward) > 0) {
                logger.debug("添加推荐人奖励设置" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(ShareReward shareReward) {
        ShareReward shareReward2 =shareRewardMapper.selectByPrimaryKey(shareReward.getId());
        if(shareReward2 == null){
            logger.warn("尝试更新推荐人奖励设置"  + " ,但是推荐人奖励设置不存在");
            return Status.NOT_EXISTS;
        }
        if (shareRewardMapper.updateByPrimaryKeySelective(shareReward) > 0) {
            logger.debug("更新推荐人奖励设置成功" + shareReward2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(ShareReward shareReward) {
        ShareReward shareReward2 = shareRewardMapper.selectByPrimaryKey(shareReward.getId());
        if (shareReward2 == null) {
            logger.warn("尝试删除推荐人奖励设置，但是推荐人奖励设置不存在");
            return Status.NOT_EXISTS;
        }
        if (shareRewardMapper.deleteByPrimaryKey(shareReward.getId()) > 0 ) {
            logger.debug("删除推荐人奖励设置成功" + shareReward.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public ShareReward get(String id) {
        return shareRewardMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(shareRewardMapper.queryShareReward(queryBase));
        queryBase.setTotalRow(shareRewardMapper.countShareReward(queryBase));
    }

    @Override
    public List<ShareReward> queryShareRewardStatus(Integer status) {
        return shareRewardMapper.queryShareRewardStatus(status);
    }

    @Override
    public ShareReward queryByUserId(String userId) {
        return shareRewardMapper.queryByUserId(userId);
    }
}
