package com.aqb.cn.utils;

import com.aqb.cn.bean.Sign;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
public class DateSeven {

    public static List<Sign> getSeven(){
        List<Sign> signs = new ArrayList<>(7);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //过去七天
        for (int i = 0; i < 7; i++) {
            c.setTime(new Date());
            c.add(Calendar.DATE, -(6 - i));
            Date d = c.getTime();
            String day = format.format(d);
            System.out.println(day);
            Sign sign = new Sign();
            sign.setDateSeven(day);
            signs.add(i,sign);
        }
        return signs;

    }

}
