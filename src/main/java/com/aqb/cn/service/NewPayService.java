package com.aqb.cn.service;

import com.aqb.cn.bean.NewPay;

/**
 * Created by Administrator on 2017/8/3.
 */
public interface NewPayService extends BaseService<NewPay> {
    boolean queryNewPayStatus(String userId);
    int updateByStatus(String userId);
}
