package com.aqb.cn.utils.Timer.barrage;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/4/0004.
 */
public class BTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("执行定时任务时的时间...");
        System.out.println(new Date());


    }
}
