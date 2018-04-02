package com.aqb.cn.pojo;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Position {

    private String devNum;
    public String getDevNum() {
        return devNum;
    }

    public void setDevNum(String devNum) {
        this.devNum = devNum;
    }

    private boolean hasPos;
    private double longitude; //经度
    private double latitude; //纬度
    private double height;
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    private double speed;
    private Date time;
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

//    public boolean isHasPos() {
//        return hasPos;
//    }

    public void setHasPos(boolean hasPos) {
        this.hasPos = hasPos;
    }

    public boolean getHasPos() {
        return this.hasPos;
    }


    public String toString(){
        return "devNo:"+this.devNum+"  longitude:"+this.longitude+"  latitude:"
                +this.latitude+"  height:"+this.height+"  speed:"+this.speed+"  time:"+this.time+"";
    }

}
