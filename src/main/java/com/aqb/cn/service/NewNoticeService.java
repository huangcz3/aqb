package com.aqb.cn.service;

import com.aqb.cn.bean.NewNotice;

/**
 * Created by Administrator on 2017/8/2.
 */
public interface NewNoticeService extends BaseService<NewNotice> {
    boolean queryNewNoticeStatus(String userId);
    int updateByStatus(String userId);
}
