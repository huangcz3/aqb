package com.aqb.cn.action;


import com.aqb.cn.service.BarrageDeleteTimeService;
import com.aqb.cn.service.BarrageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/27.
 */
@Controller
public class InitAction {

    @Autowired
    private BarrageDeleteTimeService barrageDeleteTimeService;
    @Autowired
    private BarrageService barrageService;

    //定时删除弹幕
    @PostConstruct
    public void TimedDeleteBarrage(){

        System.out.println("-------------------------------- 定时自动删除弹幕 ----------------------------------");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9); // 控制时
        calendar.set(Calendar.MINUTE, 0);    // 控制分
        calendar.set(Calendar.SECOND, 0);    // 控制秒
        Date time = calendar.getTime();     // 得出执行任务的时间,此处为09：00：00

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                //判断当前时间是否是删除时间 true则进行删除操作 false不进行删除操作
                if (barrageDeleteTimeService.isStartTime()) {
                    //删除所有弹幕
                    barrageService.deleteAll();
                    //删除完成后，将此条数据的开始删除时间再累加一个周期
                    try {
                        barrageDeleteTimeService.addCycle();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("----------------");
            }
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行

        System.out.println("-------------------------------- 定时自动删除弹幕完成 ------------------------------");

    }
}












    //开启监听端口的线程
//    @PostConstruct
//    public void portListener(){
//
//
//        server.start();
//    }

//    Server server=new Server();
//
//    //开启监听端口的线程
//    @PostConstruct
//    public void portListener(){
//
//        server.start();
//    }
//
//    // 开启一个线程，循环执行任务
//    @PostConstruct
//    public void CarState() {
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                //取得properties 文件中的值，赋给成员变量
//
//                Timer t = new Timer();
//                Calendar c = Calendar.getInstance();
//                Date firstTime = c.getTime();
//                //间隔：0.5小时
////                long period = 1000 * 60 * 30;
//                long period = 1000 * 60;//  间隔5秒
//                t.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//
//                        System.out.println("CarState  check  start  !  .....");
//                        CheckCarIsHeavy();
//
//                    }
//                }, firstTime, period);
//
//            }
//        }).start();
//
//    }
//
//    /**
//     * 每隔一段时间，执行 数据库查询
//     */
//    public void CheckCarIsHeavy() {
//
//        System.out.println("测试");
//
//
//    }
