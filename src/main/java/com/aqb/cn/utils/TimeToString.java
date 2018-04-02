package com.aqb.cn.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2017/3/30.
 */
public class TimeToString {
    /**
     * 日期转换成字符串
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 时间转换成字符串
     * @param date
     * @return str
     */
    public static String TimeToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date StrToDate2(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date StrToTime(String str) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /*
        功能：获得当前时间前30秒时间并转成字符串
        输出结果：
            系统当前时间      ：2015-09-18 10:03:00
            系统前30秒时间：2015-09-18 10:02:30

     */
    public static String Bef_ThirtySeconds(Date date){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Calendar c = new GregorianCalendar();
        //Date date = new Date();
        //System.out.println("系统当前时间      ："+df.format(date));
        c.setTime(date);//设置参数时间
        c.add(Calendar.SECOND,-60);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date=c.getTime(); //这个时间就是日期往后推一天的结果
        String str = df.format(date);
        return str;
        //System.out.println("系统前30秒时间："+str);

    }

    /*
        功能：获得当前时间后30秒时间并转成字符串
        输出结果：
            系统当前时间      ：2015-09-18 10:03:00
            系统后30秒时间：2015-09-18 10:03:30

     */
    public static String After_ThirtySeconds(Date date){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Calendar c = new GregorianCalendar();
        //Date date = new Date();
        //System.out.println("系统当前时间      ："+df.format(date));
        c.setTime(date);//设置参数时间
        c.add(Calendar.SECOND,60);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date=c.getTime(); //这个时间就是日期往后推一天的结果
        String str = df.format(date);
        return str;
        //System.out.println("系统前30秒时间："+str);

    }

    /**
     * 时间转星期
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 时间戳转为日期（yyyy-MM-dd）
     * @param dateTime
     * @return
     */
    public static Date longToDate(long dateTime) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(dateTime);
        Date date= null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 当前日期加上天数后的日期
     * @param num 为增加的天数
     * @return
     */
    public static String plusDay(int num){
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currdate = format.format(d);

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        d = ca.getTime();
        String enddate = format.format(d);

        return enddate;
    }
    /**
     * 指定日期加上天数后的日期
     * @param num 为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException
     */
    public static String plusDay2(String newDate,int num) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date  currdate = format.parse(newDate);
        //System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        //System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }
    /**
     * 指定日期加上几个月后的日期
     * @param month 为增加的天数
     * @param date 指定日期
     * @return
     * @throws ParseException
     */
    public static String plusMonth(String date,int month) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date  currdate = format.parse(date);
        //System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.MONTH, month);// month为增加的月数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        //System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }

    /**
     * 判断两个日期的年月日是否相等
     * @param d1 时间1
     * @param d2 时间2
     * @return 相等--true，不相等--false
     * @throws ParseException
     */
    public static boolean sameDate(Date d1, Date d2){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        //fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
        return fmt.format(d1).equals(fmt.format(d2));
    }

    public static void main(String[] args) throws ParseException {

        Date date = new Date();
       // Date date2 = TimeToString.StrToDate(TimeToString.After_ThirtySeconds(date));
//        System.out.println("日期转字符串：" + TimeToString.DateToStr(date));
//        System.out.println("字符串转日期：" + TimeToString.StrToDate(TimeToString.DateToStr(date)));
//        System.out.println("当前时间前30秒时间：" + TimeToString.Bef_ThirtySeconds(date));
//        System.out.println("当前时间后30秒时间：" + TimeToString.After_ThirtySeconds(date));
//        System.out.println("当前时间：" + date);
//        System.out.println("当前时间：" + TimeToString.longToDate(date.getTime()));

//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long timeNow= System.currentTimeMillis();
//        long timeLose=System.currentTimeMillis()+24*60*60*1000;
//        System.out.println(timeNow+"  "+timeNow);
//
//        String dateNow=sdf.format(new Date(timeNow));
//        String loseDate=sdf.format(new Date(timeLose));
//
//        System.out.println(sameDate(new Date(timeNow),new Date(timeLose)));
//
//        System.out.println(plusMonth(TimeToString.DateToStr(date),2));

        System.out.println(strToDate("20171129133000"));



    }
}
