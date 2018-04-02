package com.aqb.cn.utils.Timer;

import java.util.*;

/**
 * 处理时间
 * Created by Administrator on 2016/12/22.
 */
public class GetDateTime {

    public static void main(String[] args) {
        Date date = new Date(116,2,2);

        System.out.println(getYearMonth(date));
    }

    public static int getYearMonth(Date dt){//传入日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);//设置时间
        int year = cal.get(Calendar.YEAR);//获取年份

        int month=cal.get(Calendar.MONTH)+1;//获取月份,需要加1

        int week=cal.get(Calendar.DATE);//获取号数

        return year*100+month;//返回年份乘以100加上月份的值，因为月份最多2位数，所以年份乘以100可以获取一个唯一的年月数值
    }



}
