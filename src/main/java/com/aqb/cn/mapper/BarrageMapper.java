package com.aqb.cn.mapper;

import com.aqb.cn.bean.Barrage;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.pojo.BarrageAndUser;

import java.util.List;

public interface BarrageMapper {

    int deleteByPrimaryKey(String id);

    int insert(Barrage record);

    int insertSelective(Barrage record);

    Barrage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Barrage record);

    int updateByPrimaryKey(Barrage record);

    List<Barrage> queryAll();

    List<BarrageAndUser> queryBarrage(QueryBase queryBase);
    long countBarrage(QueryBase queryBase);

    //List<Barrage> queryBarrageByTime(Date date1,Date date2);
    List<Barrage> queryBarrageByTime(String str_before,String str_after);

    List<Barrage> queryByTopicId(String topicId);
}