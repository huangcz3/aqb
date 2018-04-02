package com.aqb.cn.pojo;

import com.aqb.cn.bean.AdverDate;

import java.util.Date;
import java.util.List;

/**
 * 查询广告详情的展示类
 */
public class AdverGet {

    private String content;//广告内容

    private Date foundDate;//投放时间

    private float daquanMoney;//大圈投放金额

    private float daquanJifen;//大圈投放积分

    private Integer daquanNumber;//大圈投放条数

    private Float daquanDanjia;//大圈投放单价

    private Integer daquants;//大圈已投放条数

    private Float neiquanMoney;//内圈投放金额

    private Integer neiquanNumber;//内圈投放条数

    private Float neiquanDanjia;//内圈投放单价

    private Integer neiquants;//内圈已投放条数

    private List<AdverDate> adverDates;//投放时间段

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Date foundDate) {
        this.foundDate = foundDate;
    }

    public float getDaquanMoney() {
        return daquanMoney;
    }

    public void setDaquanMoney(float daquanMoney) {
        this.daquanMoney = daquanMoney;
    }

    public float getDaquanJifen() {
        return daquanJifen;
    }

    public void setDaquanJifen(float daquanJifen) {
        this.daquanJifen = daquanJifen;
    }

    public Integer getDaquanNumber() {
        return daquanNumber;
    }

    public void setDaquanNumber(Integer daquanNumber) {
        this.daquanNumber = daquanNumber;
    }

    public Float getDaquanDanjia() {
        return daquanDanjia;
    }

    public void setDaquanDanjia(Float daquanDanjia) {
        this.daquanDanjia = daquanDanjia;
    }

    public Integer getDaquants() {
        return daquants;
    }

    public void setDaquants(Integer daquants) {
        this.daquants = daquants;
    }

    public Float getNeiquanMoney() {
        return neiquanMoney;
    }

    public void setNeiquanMoney(Float neiquanMoney) {
        this.neiquanMoney = neiquanMoney;
    }

    public Integer getNeiquanNumber() {
        return neiquanNumber;
    }

    public void setNeiquanNumber(Integer neiquanNumber) {
        this.neiquanNumber = neiquanNumber;
    }

    public Float getNeiquanDanjia() {
        return neiquanDanjia;
    }

    public void setNeiquanDanjia(Float neiquanDanjia) {
        this.neiquanDanjia = neiquanDanjia;
    }

    public Integer getNeiquants() {
        return neiquants;
    }

    public void setNeiquants(Integer neiquants) {
        this.neiquants = neiquants;
    }

    public List<AdverDate> getAdverDates() {
        return adverDates;
    }

    public void setAdverDates(List<AdverDate> adverDates) {
        this.adverDates = adverDates;
    }
}
