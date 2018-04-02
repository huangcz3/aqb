package com.aqb.cn.mapper;

import com.aqb.cn.bean.Topic;

import java.util.List;

public interface TopicMapper {
    int deleteByPrimaryKey(String id);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKey(Topic record);

    List<Topic> queryTopic();
}