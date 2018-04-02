package com.aqb.cn.service.impl;

import com.aqb.cn.bean.BarrageDeleteTime;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.BarrageDeleteTimeMapper;
import com.aqb.cn.service.BarrageDeleteTimeService;
import com.aqb.cn.utils.TimeToString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/9/0009.
 */
@Service
public class BarrageDeleteTimeServiceImpl implements BarrageDeleteTimeService {

    private Log logger = LogFactory.getLog(BarrageDeleteTimeServiceImpl.class);

    @Autowired
    private BarrageDeleteTimeMapper barrageDeleteTimeMapper;

    @Override
    public int add(BarrageDeleteTime barrageDeleteTime) {
        BarrageDeleteTime barrageDeleteTime_db = barrageDeleteTimeMapper.selectByPrimaryKey(barrageDeleteTime.getId());
        if (barrageDeleteTime_db == null){
            if (barrageDeleteTimeMapper.insertSelective(barrageDeleteTime) > 0){
                logger.debug("添加弹幕定时删除时间" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(BarrageDeleteTime barrageDeleteTime) {
        BarrageDeleteTime barrageDeleteTime_db = barrageDeleteTimeMapper.selectByPrimaryKey(barrageDeleteTime.getId());
        if (barrageDeleteTime_db == null){
            logger.warn("尝试更新弹幕定时删除时间，但此时间不存在");
            return Status.NOT_EXISTS;
        }
        if (barrageDeleteTimeMapper.updateByPrimaryKeySelective(barrageDeleteTime) > 0){
            logger.debug("更新弹幕定时删除时间成功" + barrageDeleteTime_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(BarrageDeleteTime barrageDeleteTime) {
        BarrageDeleteTime barrageDeleteTime_db = barrageDeleteTimeMapper.selectByPrimaryKey(barrageDeleteTime.getId());
        if (barrageDeleteTime_db == null){
            logger.warn("尝试删除弹幕定时删除时间，但此时间不存在");
            return Status.NOT_EXISTS;
        }
        if (barrageDeleteTimeMapper.deleteByPrimaryKey(barrageDeleteTime.getId()) > 0){
            logger.debug("删除弹幕定时删除时间成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public BarrageDeleteTime get(String id) {
        return barrageDeleteTimeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

    }

    @Override
    public List<BarrageDeleteTime> queryBarrageDeleteTime() {
        List<BarrageDeleteTime> barrageDeleteTimeList = barrageDeleteTimeMapper.queryBarrageDeleteTime();
        if (barrageDeleteTimeList == null){
            return null;
        }
        return barrageDeleteTimeList;
    }

    @Override
    public BarrageDeleteTime selectInUse() {
        return barrageDeleteTimeMapper.selectInUse();
    }

    @Override
    public boolean isStartTime() {
        //查询正在启用的弹幕定时删除时间
        BarrageDeleteTime barrageDeleteTime = barrageDeleteTimeMapper.selectInUse();
        if (barrageDeleteTime == null){
            return false;
        }

        Date currentTime = new Date();
        Date startTime = barrageDeleteTime.getStartTime();

        //判断当前时间是否为执行时间，只判断年月日
        if (TimeToString.sameDate(currentTime,startTime)){
            return true;
        }
        return false;
    }

    @Override
    public void addCycle() throws ParseException {
        //查询正在启用的弹幕定时删除时间
        BarrageDeleteTime barrageDeleteTime = barrageDeleteTimeMapper.selectInUse();

        Date startTime = barrageDeleteTime.getStartTime();
        Date reStartTime = null;
        String strDate = "";
        String name = barrageDeleteTime.getName();

        if (name.equals("一周")){
            //一周---7天一个周期
            strDate = TimeToString.plusDay2(TimeToString.DateToStr(startTime),7);
        }
        if (name.equals("一月")){
            //一个月---1个月为一个周期
            strDate = TimeToString.plusMonth(TimeToString.DateToStr(startTime), 1);
        }
        if (name.equals("一季度")){
            //一个月---3个月为一个周期
            strDate = TimeToString.plusMonth(TimeToString.DateToStr(startTime), 3);
        }
        if (name.equals("一年")){
            //一个月---12个月为一个周期
            strDate = TimeToString.plusMonth(TimeToString.DateToStr(startTime), 12);
        }
        reStartTime = TimeToString.StrToDate(strDate);
        System.out.println(reStartTime);
        barrageDeleteTime.setStartTime(reStartTime);
        barrageDeleteTimeMapper.updateByPrimaryKeySelective(barrageDeleteTime);

    }

    @Override
    public void useOtherBarrageDeleteTime(String id) throws ParseException {

        BarrageDeleteTime barrageDeleteTimeInUse = barrageDeleteTimeMapper.selectInUse();//查询正在启用的弹幕定时删除时间
        BarrageDeleteTime barrageDeleteTime = barrageDeleteTimeMapper.selectByPrimaryKey(id);//需要启用的弹幕定时删除时间

        if (barrageDeleteTimeInUse != null){
            barrageDeleteTimeInUse.setStatus(0);//停用
            barrageDeleteTimeMapper.updateByPrimaryKeySelective(barrageDeleteTimeInUse);
        }

        Date date = new Date();
        Date startTime = null;
        String strDate = "";
        String name = barrageDeleteTime.getName();

        if (name.equals("一周")){
            //一周---7天一个周期
            strDate = TimeToString.plusDay2(TimeToString.DateToStr(date),7);
        }
        if (name.equals("一月")){
            //一个月---1个月为一个周期
            strDate = TimeToString.plusMonth(TimeToString.DateToStr(date), 1);
        }
        if (name.equals("一季度")){
            //一个月---3个月为一个周期
            strDate = TimeToString.plusMonth(TimeToString.DateToStr(date), 3);
        }
        if (name.equals("一年")){
            //一个月---12个月为一个周期
            strDate = TimeToString.plusMonth(TimeToString.DateToStr(date), 12);
        }
        startTime = TimeToString.StrToDate(strDate);
        System.out.println(startTime);

        barrageDeleteTime.setStartTime(startTime);
        barrageDeleteTime.setStatus(1);//启用
        barrageDeleteTimeMapper.updateByPrimaryKeySelective(barrageDeleteTime);
    }


}
