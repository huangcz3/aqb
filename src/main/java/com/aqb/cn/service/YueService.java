package com.aqb.cn.service;

import com.aqb.cn.bean.Yue;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public interface YueService extends BaseService<Yue>{
    List<Yue> queryYue();

    List<Yue> queryYueByUserId(String userId);


    int updateYueToJifen(String userId, Float cost_money, Float ye);
}
