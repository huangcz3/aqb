package com.aqb.cn.service;

import com.aqb.cn.bean.ProfitPlatform;

/**
 * Created by Administrator on 2017/8/10.
 */
public interface ProfitPlatformService extends BaseService<ProfitPlatform> {
    Float sumMoneyByYear(Integer status, String year);
    Float sumMoneyByMonth(Integer status, String month);
    Float sumMoneyByDay(Integer status, String day);
}
