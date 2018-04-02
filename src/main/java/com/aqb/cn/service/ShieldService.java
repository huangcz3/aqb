package com.aqb.cn.service;

import com.aqb.cn.bean.Shield;

import java.util.List;


public interface ShieldService extends BaseService<Shield>{
    List<Shield> selectShieldList();
}
