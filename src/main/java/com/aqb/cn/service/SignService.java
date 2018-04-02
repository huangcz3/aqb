package com.aqb.cn.service;

import com.aqb.cn.bean.Sign;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */
public interface SignService extends BaseService<Sign>{
    List<Sign> querysign();

    List<Sign> querySignByUserId(String userId);
    List<Sign> querySevenSign(String userId);
    Sign querytodaySign(String userId);
}
