package com.aqb.cn.service;

import com.aqb.cn.bean.Vip;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
public interface VipService extends BaseService<Vip> {
    List<Vip> queryvip();

    Vip selectByGrade(Integer vipGrade);
}
