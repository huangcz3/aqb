package com.aqb.cn.service;

import com.aqb.cn.bean.Price;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
public interface PriceService extends BaseService<Price> {
    List<Price> queryPrice(String priceCity);
    Price selectJiage();
}
