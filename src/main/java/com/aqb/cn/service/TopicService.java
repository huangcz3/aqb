package com.aqb.cn.service;

import com.aqb.cn.bean.Topic;

import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */
public interface TopicService extends BaseService<Topic> {
    List<Topic> queryTopic();
}
