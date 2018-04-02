package com.aqb.cn.service;

import com.aqb.cn.bean.BarrageDeleteTime;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2017/8/9/0009.
 */
public interface BarrageDeleteTimeService extends BaseService<BarrageDeleteTime> {
    List<BarrageDeleteTime> queryBarrageDeleteTime();

    BarrageDeleteTime selectInUse();

    boolean isStartTime();//判断当前时间是否是删除时间

    void addCycle() throws ParseException;//将此条数据的开始删除时间再累加一个周期

    void useOtherBarrageDeleteTime(String id) throws ParseException;//使用其他的定时删除时间
}
