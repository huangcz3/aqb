package com.aqb.cn.service;

import com.aqb.cn.bean.ShareReward;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */
public interface ShareRewardService extends BaseService<ShareReward> {
    List<ShareReward> queryShareRewardStatus(Integer status);
    ShareReward queryByUserId(String userId);
}
