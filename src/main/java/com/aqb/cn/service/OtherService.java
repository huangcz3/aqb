package com.aqb.cn.service;

import com.aqb.cn.bean.Other;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */
public interface OtherService extends BaseService<Other> {
    List<Other> queryOther();
}
