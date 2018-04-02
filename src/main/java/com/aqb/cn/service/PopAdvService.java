package com.aqb.cn.service;

import com.aqb.cn.bean.PopAdv;
import com.aqb.cn.pojo.PopAndPara;

/**
 * Created by Administrator on 2017/8/4/0004.
 */
public interface PopAdvService extends BaseService<PopAdv>{
    PopAndPara queryPopAdv();

    PopAndPara queryappPopAdv();
}
