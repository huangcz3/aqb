package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Barrage;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.BarrageMapper;
import com.aqb.cn.mapper.TopicMapper;
import com.aqb.cn.pojo.BarrageAndUser;
import com.aqb.cn.pojo.TopicAndBarrage;
import com.aqb.cn.service.BarrageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
@Service
public class BarrageServiceImpl implements BarrageService {

    private Log logger = LogFactory.getLog(BarrageServiceImpl.class);

    @Autowired
    private BarrageMapper barrageMapper;
    @Autowired
    private TopicMapper topicMapper;


    @Override
    public int add(Barrage barrage) {
        Barrage barrage_db = barrageMapper.selectByPrimaryKey(barrage.getId());
        if(barrage_db == null) {
            if (barrageMapper.insertSelective(barrage) > 0) {
                logger.debug("添加弹幕" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Barrage barrage) {
        Barrage barrage2 =barrageMapper.selectByPrimaryKey(barrage.getId());
        if(barrage2 == null){
            logger.warn("尝试更新弹幕"  + " ,但是弹幕不存在");
            return Status.NOT_EXISTS;
        }
        if (barrageMapper.updateByPrimaryKeySelective(barrage) > 0) {
            logger.debug("更新弹幕成功" + barrage2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Barrage barrage) {
        Barrage barrage2 = barrageMapper.selectByPrimaryKey(barrage.getId());
        if (barrage2 == null) {
            logger.warn("尝试删除弹幕，但是弹幕不存在");
            return Status.NOT_EXISTS;
        }
        if (barrageMapper.deleteByPrimaryKey(barrage.getId()) > 0 ) {
            logger.debug("删除弹幕成功" + barrage.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int deleteAll() {
        int status = 0;
        List<Barrage> barrageList = barrageMapper.queryAll();
        if (barrageList.size() == 0){
            logger.warn("弹幕为空");
            return Status.NOT_EXISTS;
        }
        for (Barrage barrage:barrageList){
            if (barrageMapper.deleteByPrimaryKey(barrage.getId()) > 0){
                status ++;
                logger.debug("删除弹幕成功" + barrage.getId());
            }
        }
        if (status > 0) {
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }


    @Override
    public Barrage get(String id) {
        return barrageMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        List<BarrageAndUser> barrages = barrageMapper.queryBarrage(queryBase);
        List<TopicAndBarrage> topicAndBarrages = new ArrayList<>();//返回的结果集
        for(BarrageAndUser barrageAndUser : barrages){
            boolean key = false;
            //判断是否已经添加到返回的结果集
            for(TopicAndBarrage topicAndBarrage : topicAndBarrages) {
                if(topicAndBarrage.getTopicTitle().equals(barrageAndUser.getTopicTitle())){
                    //已经添加，加入已存在的List
                    topicAndBarrage.getBarrageAndUsers().add(barrageAndUser);
                    key = true;
                    break;
                }
            }
            if(key){
                continue;
            }
            //没有添加，新建一个
            TopicAndBarrage t = new TopicAndBarrage();
            t.setTopicTitle(barrageAndUser.getTopicTitle());
            List<BarrageAndUser> b = new ArrayList<>();
            b.add(barrageAndUser);
            t.setBarrageAndUsers(b);
            topicAndBarrages.add(t);
        }
        queryBase.setResults(topicAndBarrages);
        queryBase.setTotalRow(barrageMapper.countBarrage(queryBase));
    }

    @Override
    public List<Barrage> queryBarrageByTime(String str_before,String str_after) {
        return  barrageMapper.queryBarrageByTime(str_before, str_after);
    }

    @Override
    public List<Barrage> queryByTopicId(String topicId) {
        return barrageMapper.queryByTopicId(topicId);
    }


}
