package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Barrage;
import com.aqb.cn.bean.Topic;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.TopicMapper;
import com.aqb.cn.service.BarrageService;
import com.aqb.cn.service.TopicService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */
@Service
public class TopicServiceImpl implements TopicService {

    private Log logger = LogFactory.getLog(TopicServiceImpl.class);

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private BarrageService barrageService;


    @Override
    public int add(Topic topic) {
        Topic topic_db = topicMapper.selectByPrimaryKey(topic.getId());
        if(topic_db == null) {
            if (topicMapper.insertSelective(topic) > 0) {
                logger.debug("添加话题" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Topic topic) {
        Topic topic2 =topicMapper.selectByPrimaryKey(topic.getId());
        if(topic2 == null){
            logger.warn("尝试更新话题"  + " ,但是话题不存在");
            return Status.NOT_EXISTS;
        }
        if (topicMapper.updateByPrimaryKeySelective(topic) > 0) {
            logger.debug("更新话题成功" + topic2.getId());
            List<Barrage> barrages = barrageService.queryByTopicId(topic.getId());
            if (barrages.size() > 0){
                for (Barrage barrage:barrages){
                    barrage.setTopicId(topic.getId());
                    barrageService.update(barrage);
                }
            }
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Topic topic) {
        Topic topic2 = topicMapper.selectByPrimaryKey(topic.getId());
        if (topic2 == null) {
            logger.warn("尝试删除话题，但是话题不存在");
            return Status.NOT_EXISTS;
        }
        if (topicMapper.deleteByPrimaryKey(topic.getId()) > 0 ) {
            logger.debug("删除话题成功" + topic.getId());
            List<Barrage> barrages = barrageService.queryByTopicId(topic.getId());
            if (barrages.size() >0){
                for (Barrage barrage:barrages){
                    barrageService.delete(barrage);
                }
            }
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Topic get(String id) {
        return topicMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<Topic> queryTopic() {
        return topicMapper.queryTopic();
    }
}
