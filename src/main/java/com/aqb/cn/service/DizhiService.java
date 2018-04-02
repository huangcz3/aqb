package com.aqb.cn.service;

import com.aqb.cn.bean.Dizhi;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
public interface DizhiService extends BaseService<Dizhi> {
    List<Dizhi> queryDizhi(String userId);
    Dizhi queryByUserId(String userId);
}
