package com.aqb.cn.utils.Timer.vip;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by Administrator on 2017/8/2/0002.
 */
public class VipTimer implements ServletContextListener {

    //时间间隔(一天)
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    /**
     * 定时器
     */
    private Timer timer;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23); //凌晨1点
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date date=calendar.getTime(); //第一次执行定时任务的时间
        System.out.println(date);
        System.out.println("----------------------------------------------");
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }
        VipTimerTask task = new VipTimerTask();
        timer = new Timer("用户会员计时",true);
        System.out.println("===============================================");
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(task,date,PERIOD_DAY);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (timer!=null){
            timer.cancel();
        }
    }
    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
//    public static void main(String[] args) {
//        new VipTimer();
//    }


}
//class MyVipTimer extends Timer{
//    //时间间隔(一天)
//    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
//    public MyVipTimer() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 23); //凌晨1点
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        Date date=calendar.getTime(); //第一次执行定时任务的时间
//        System.out.println(date);
//        //如果第一次执行定时任务的时间 小于当前的时间
//        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
//        if (date.before(new Date())) {
//            date = this.addDay(date, 1);
//        }
//        Timer timer = new Timer();
//        VipTimerTask task = new VipTimerTask();
//        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
//        timer.schedule(task,new Date(),PERIOD_DAY);
//    }
//
//    // 增加或减少天数
//    public Date addDay(Date date, int num) {
//        Calendar startDT = Calendar.getInstance();
//        startDT.setTime(date);
//        startDT.add(Calendar.DAY_OF_MONTH, num);
//        return startDT.getTime();
//    }
//}
