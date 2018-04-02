package com.aqb.cn.utils.Money;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/14.
 */
public class Exchangeutil {

    /**
     * 积分转换成金额
     * @param 积分
     * @return str
     */
    public static String JifenToYue(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 金额转换成积分
     * @param 积分
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

}
