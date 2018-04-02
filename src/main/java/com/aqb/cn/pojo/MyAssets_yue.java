package com.aqb.cn.pojo;

import com.aqb.cn.bean.Yue;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public class MyAssets_yue {

    private List<Yue> yues;

    //private List<Jifen> jifens;

    private String yue_Assets;

    //private int jifen_Assets;

    private Boolean aBoolean;//是否缴纳押金，t--已缴，f-未缴

    private Float aFloat;//缴纳的押金金额

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public List<Yue> getYues() {
        return yues;
    }

    public void setYues(List<Yue> yues) {
        this.yues = yues;
    }



    public String getYue_Assets() {
        return yue_Assets;
    }

    public void setYue_Assets(String yue_Assets) {
        this.yue_Assets = yue_Assets;
    }


}
