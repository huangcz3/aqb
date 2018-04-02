package com.aqb.cn.utils.Timer;


import com.aqb.cn.bean.Advertisement;
import com.aqb.cn.bean.Delivery;
import com.aqb.cn.mapper.AdvertisementMapper;
import com.aqb.cn.mapper.DeliveryMapper;
import com.aqb.cn.pojo.Position;
import com.aqb.cn.utils.*;
import com.aqb.cn.utils.ApplicationContext.MyApplicationContextUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.*;

/**
 * 定时任务
 * Created by Administrator on 2016/12/1.
 */
public class TimerPush extends TimerTask {


    private String id;

    public TimerPush(String id){
        this.id = id;
    }

    public static void main(String[] args) {
        Timer timer;
        timer = new Timer();
        timer.schedule(new TimerPush("74BB02C0369146D9B2C919195E150844"), 1000);
    }

    //执行的任务
    @Override
    public void run() {

        System.out.println("执行定时任务时的时间...");
        System.out.println(new Date().getTime());
        System.out.println(id);

        AdvertisementMapper advertisementMapper = (AdvertisementMapper) MyApplicationContextUtil.getContext().getBean("advertisementMapper");

        DeliveryMapper deliveryMapper = (DeliveryMapper) MyApplicationContextUtil.getContext().getBean("deliveryMapper");

        //通过id，查询出该广告
        Advertisement advertisement = advertisementMapper.selectByPrimaryKey(id);
        Set set = new HashSet();
        //获取redis中保存的车辆坐标集合
        List<Position> positions = GetPositionList.getPositionList();
        for(Position position : positions){
            //计算车辆位置与，圈的圆心位置的距离
            double s = GPSDistance.GetDistance(advertisement.getWeidu(), advertisement.getJingdu(),
                    position.getLatitude(), position.getLongitude());
            //根据距离判断车是否在圈内
            if(advertisement.getDaquan() > s){
                //在圈内的车，加入set集合
                set.add(position.getDevNum());
            }
        }
        //给列表用户发送消息
        String sendMsg="msg="+advertisement.getAdvertisementContent()+"&list="+set;
        String response="";
        try{
            response = GetPositionList.sendPost("http://127.0.0.1:8888/aqb/tests/sendMsg",sendMsg);
        }catch (IOException io){
            System.out.println("给列表用户发送消息时异常");
            io.printStackTrace();
        }
        //解析接收到的json响应
        JsonParser parser=new JsonParser();
        JsonObject object=(JsonObject) parser.parse(response);
        JsonElement status = object.get("status");
        if(status.equals(0)){
            System.out.println("发送成功");
            //更新广告发送表数据
            Date date = new Date();
            for (Object devNum : set) {
                Delivery delivery = new Delivery();
                delivery.setId(UUIDCreator.getUUID());
                delivery.setAdvertisementId(advertisement.getId());
                delivery.setUserId((String) devNum);
                delivery.setStatus(1);
                delivery.setFoundDate(date);
                deliveryMapper.insertSelective(delivery);
            }


        }else {
            System.out.println("发送失败");
        }

    }
}
