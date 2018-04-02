package com.aqb.cn.mapper;

import com.aqb.cn.bean.ShareReward;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface ShareRewardMapper {
    int deleteByPrimaryKey(String id);

    int insert(ShareReward record);

    int insertSelective(ShareReward record);

    ShareReward selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShareReward record);

    int updateByPrimaryKey(ShareReward record);

    List<ShareReward> queryShareRewardStatus(Integer status);

    List<ShareReward> queryShareReward(QueryBase queryBase);
    long countShareReward(QueryBase queryBase);

    ShareReward queryByUserId(String userId);
}