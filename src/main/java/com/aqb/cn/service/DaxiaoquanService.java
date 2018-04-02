package com.aqb.cn.service;

import com.aqb.cn.bean.Daxiaoquan;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
public interface DaxiaoquanService extends BaseService<Daxiaoquan> {
    List<Daxiaoquan> queryDaxiaoquan(Integer status);
    List<Daxiaoquan> queryDaxiaoquanStatus(Integer status, Integer xulie);
}
