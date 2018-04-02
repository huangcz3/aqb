package com.aqb.cn.service;

import com.aqb.cn.bean.Proportion;

import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */
public interface ProportionService extends BaseService<Proportion> {
    List<Proportion> queryProportion();
    List<Proportion> selectByStatus(Integer status);
}
