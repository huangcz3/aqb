package com.aqb.cn.service;

import com.aqb.cn.bean.BarrageLike;

/**
 * Created by Administrator on 2017/7/21/0021.
 */
public interface BarrageLikeService extends BaseService<BarrageLike>{
    BarrageLike queryByUserIdBarrageId(String userId,String barrageId);
}
