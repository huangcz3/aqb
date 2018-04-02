package com.aqb.cn.service;

import com.aqb.cn.bean.Repair;
import com.aqb.cn.common.QueryBase;

/**
 * Created by Administrator on 2017/6/28.
 */
public interface RepairService extends BaseService<Repair>{

    void queryShop(QueryBase queryBase);

    Repair queryById(String id);
}
