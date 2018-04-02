package com.aqb.cn.bean;

import java.util.Date;

public class Vip {
    private String id;

    private String vipName;

    private Integer vipGrade;//会员等级（1-ViP1，2-Vip2,3-Vip-3）

    private Float vipMoney;//会员价格

    private Integer vipJifen;//会员积分

    private Boolean green;

    private Boolean yellow;

    private Boolean red;

    private Boolean blue;

    private Boolean bottom;

    private Boolean top;

    private Boolean send;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName == null ? null : vipName.trim();
    }

    public Integer getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(Integer vipGrade) {
        this.vipGrade = vipGrade;
    }

    public Float getVipMoney() {
        return vipMoney;
    }

    public void setVipMoney(Float vipMoney) {
        this.vipMoney = vipMoney;
    }

    public Integer getVipJifen() {
        return vipJifen;
    }

    public void setVipJifen(Integer vipJifen) {
        this.vipJifen = vipJifen;
    }

    public Boolean getGreen() {
        return green;
    }

    public void setGreen(Boolean green) {
        this.green = green;
    }

    public Boolean getYellow() {
        return yellow;
    }

    public void setYellow(Boolean yellow) {
        this.yellow = yellow;
    }

    public Boolean getRed() {
        return red;
    }

    public void setRed(Boolean red) {
        this.red = red;
    }

    public Boolean getBlue() {
        return blue;
    }

    public void setBlue(Boolean blue) {
        this.blue = blue;
    }

    public Boolean getBottom() {
        return bottom;
    }

    public void setBottom(Boolean bottom) {
        this.bottom = bottom;
    }

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
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