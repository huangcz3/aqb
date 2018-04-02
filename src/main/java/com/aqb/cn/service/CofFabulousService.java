package com.aqb.cn.service;

import com.aqb.cn.bean.CofFabulous;

/**
 * Created by Administrator on 2017/6/27.
 */
public interface CofFabulousService extends  BaseService<CofFabulous>{
    CofFabulous queryByUserIdCofId(String userId,String cofId);
}
