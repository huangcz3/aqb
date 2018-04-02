package com.aqb.cn.utils.Timer;

/**
 * Created by Administrator on 2017/6/23.
 */
public class TimeToMillisecond {

    public static void main(String[] args) {
        System.out.println(getMillisecond("00:30:12"));
    }

    /**
     * 时间转为毫秒（或秒）
     * @param time
     * @return
     */
    public static int getMillisecond(String time){
        String[] my =time.split(":");
        int hour =Integer.parseInt(my[0]);
        int min =Integer.parseInt(my[1]);
        int sec =Integer.parseInt(my[2]);

        int millisecond =hour*3600+min*60+sec;
        return millisecond;
    }

}
