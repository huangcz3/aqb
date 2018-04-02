package com.aqb.cn.utils.Timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/23.
 */
public class TestTime {

    public static void main(String[] args) {
        String string = "013000";
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HHmmss");
        Long millionSeconds = null;
        try {
            millionSeconds = df.parse(string).getTime();//毫秒��
        }catch (ParseException p){
            p.printStackTrace();
        }

        System.out.println(millionSeconds);
    }


}
