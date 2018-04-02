package com.aqb.cn.utils.Timer;



import java.util.Date;
import java.util.Timer;

/**
 * Created by Administrator on 2016/12/1.
 */
public class TimerTest {

    Timer timer;
    //time:延迟时间 id:广告id
    public TimerTest(long time,String id){
        timer = new Timer();
        timer.schedule(new TimerPush(id), time);
    }

    public static void main(String[] args) {
        System.out.println("设置定时任务时的时间....");
        System.out.println(new Date().getTime());
        new TimerTest(1000, "74BB02C0369146D9B2C919195E150844");
    }
}
