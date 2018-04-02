package com.aqb.cn.service;

import com.aqb.cn.bean.Delivery;

import java.util.List;

/**
 * Created by Administrator on 2017/6/24.
 */
public interface DeliveryService extends BaseService<Delivery> {
    List<Delivery> queryRed(String userId);
    int updateRed(List<Delivery> deliverys,String userId);
}
