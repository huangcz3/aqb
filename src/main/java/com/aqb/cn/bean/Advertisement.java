package com.aqb.cn.bean;

import java.util.Date;

public class Advertisement {
    private String id;

    private String advertisementContent;

    private String userId;

    private Double jingdu;

    private Double weidu;

    private Float daquan;

    private Integer daquanMoneyStatus;

    private Float daquanMoneyNumber;

    private Float daquanJifen;

    private Integer daquanTiaoshu;

    private Float daquanDanjia;

    private Integer daquanStatus;

    private Float neiquan;

    private Integer neiquanMoneyStatus;

    private Float neiquanMoneyNumber;

    private Integer neiquanTiaoshu;

    private Float neiquanDanjia;

    private Integer neiquanStatus;

    private Integer payStatus;

    private Integer shStatus;

    private String bhContent;

    private Integer rfStatus;

    private Integer status;

    private Date foundDate;

    private Integer key;//该状态用来标记车辆是否在小圈内：1--在大圈内，2--在小圈内

    private User user;

    private String daquanColour;//大圈颜色

    private String neiquanColour;//内圈颜色

    public String getDaquanColour() {
        return daquanColour;
    }

    public void setDaquanColour(String daquanColour) {
        this.daquanColour = daquanColour;
    }

    public String getNeiquanColour() {
        return neiquanColour;
    }

    public void setNeiquanColour(String neiquanColour) {
        this.neiquanColour = neiquanColour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAdvertisementContent() {
        return advertisementContent;
    }

    public void setAdvertisementContent(String advertisementContent) {
        this.advertisementContent = advertisementContent == null ? null : advertisementContent.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Double getJingdu() {
        return jingdu;
    }

    public void setJingdu(Double jingdu) {
        this.jingdu = jingdu;
    }

    public Double getWeidu() {
        return weidu;
    }

    public void setWeidu(Double weidu) {
        this.weidu = weidu;
    }

    public Float getDaquan() {
        return daquan;
    }

    public void setDaquan(Float daquan) {
        this.daquan = daquan;
    }

    public Integer getDaquanMoneyStatus() {
        return daquanMoneyStatus;
    }

    public void setDaquanMoneyStatus(Integer daquanMoneyStatus) {
        this.daquanMoneyStatus = daquanMoneyStatus;
    }

    public Float getDaquanMoneyNumber() {
        return daquanMoneyNumber;
    }

    public void setDaquanMoneyNumber(Float daquanMoneyNumber) {
        this.daquanMoneyNumber = daquanMoneyNumber;
    }

    public Float getDaquanJifen() {
        return daquanJifen;
    }

    public void setDaquanJifen(Float daquanJifen) {
        this.daquanJifen = daquanJifen;
    }

    public Integer getDaquanTiaoshu() {
        return daquanTiaoshu;
    }

    public void setDaquanTiaoshu(Integer daquanTiaoshu) {
        this.daquanTiaoshu = daquanTiaoshu;
    }

    public Float getDaquanDanjia() {
        return daquanDanjia;
    }

    public void setDaquanDanjia(Float daquanDanjia) {
        this.daquanDanjia = daquanDanjia;
    }

    public Integer getDaquanStatus() {
        return daquanStatus;
    }

    public void setDaquanStatus(Integer daquanStatus) {
        this.daquanStatus = daquanStatus;
    }

    public Float getNeiquan() {
        return neiquan;
    }

    public void setNeiquan(Float neiquan) {
        this.neiquan = neiquan;
    }

    public Integer getNeiquanMoneyStatus() {
        return neiquanMoneyStatus;
    }

    public void setNeiquanMoneyStatus(Integer neiquanMoneyStatus) {
        this.neiquanMoneyStatus = neiquanMoneyStatus;
    }

    public Float getNeiquanMoneyNumber() {
        return neiquanMoneyNumber;
    }

    public void setNeiquanMoneyNumber(Float neiquanMoneyNumber) {
        this.neiquanMoneyNumber = neiquanMoneyNumber;
    }

    public Integer getNeiquanTiaoshu() {
        return neiquanTiaoshu;
    }

    public void setNeiquanTiaoshu(Integer neiquanTiaoshu) {
        this.neiquanTiaoshu = neiquanTiaoshu;
    }

    public Float getNeiquanDanjia() {
        return neiquanDanjia;
    }

    public void setNeiquanDanjia(Float neiquanDanjia) {
        this.neiquanDanjia = neiquanDanjia;
    }

    public Integer getNeiquanStatus() {
        return neiquanStatus;
    }

    public void setNeiquanStatus(Integer neiquanStatus) {
        this.neiquanStatus = neiquanStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getShStatus() {
        return shStatus;
    }

    public void setShStatus(Integer shStatus) {
        this.shStatus = shStatus;
    }

    public String getBhContent() {
        return bhContent;
    }

    public void setBhContent(String bhContent) {
        this.bhContent = bhContent == null ? null : bhContent.trim();
    }

    public Integer getRfStatus() {
        return rfStatus;
    }

    public void setRfStatus(Integer rfStatus) {
        this.rfStatus = rfStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Date foundDate) {
        this.foundDate = foundDate;
    }
}