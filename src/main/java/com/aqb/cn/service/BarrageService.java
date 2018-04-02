package com.aqb.cn.service;

import com.aqb.cn.bean.Barrage;

import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public interface BarrageService extends BaseService<Barrage> {
    //List<Barrage> queryBarrageByTime(Date date1,Date date2);
    List<Barrage> queryBarrageByTime(String str_before,String str_after);

    List<Barrage> queryByTopicId(String topicId);//查询当前话题下的所有弹幕

    int deleteAll();//删除所有弹幕
}
