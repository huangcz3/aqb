package com.aqb.cn.service;

import com.aqb.cn.bean.Cof;
import com.aqb.cn.common.QueryBase;

/**
 * Created by Administrator on 2017/6/27.
 */
public interface CofService extends BaseService<Cof> {
    public void querycof_space(QueryBase queryBase);
}
