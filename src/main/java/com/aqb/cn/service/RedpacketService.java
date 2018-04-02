package com.aqb.cn.service;

import com.aqb.cn.bean.Redpacket;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
public interface RedpacketService extends BaseService<Redpacket>{
    List<Redpacket> queryRedpacketByUserId(String userId);

    Float countAsessts(String userId);

    Float redTo(Float score,Integer status);

    int updateRedpactToYue(String userId, Float redpacket, Float yue);
}
