package com.aqb.cn.service;

import com.aqb.cn.bean.Jifen;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public interface JifenService extends BaseService<Jifen>{
    List<Jifen> queryJifen();

    List<Jifen> queryJifenByUserId(String userId);

    int updateJifenToMoney(String userId,Float jifen,Float money);

    int updateJifenToRedpacket(String userId,Float jifen,Float money);

}
