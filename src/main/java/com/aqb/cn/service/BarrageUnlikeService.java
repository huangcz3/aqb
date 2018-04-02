package com.aqb.cn.service;

import com.aqb.cn.bean.BarrageUnlike;

/**
 * Created by Administrator on 2017/7/21/0021.
 */
public interface BarrageUnlikeService extends BaseService<BarrageUnlike>{
    BarrageUnlike queryByUserIdBarrageId(String userId,String barrageId);
}
