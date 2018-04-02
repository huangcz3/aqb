package com.aqb.cn.bean;

import java.util.Date;

public class Yue {
    private String id;

    private String userId;

    private Integer yueCategory;

    private Boolean yueIncomeOut;

    private Float yueSubtotal;

    private Integer yueStatus;

    private Date yueFoudDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getYueCategory() {
        return yueCategory;
    }

    public void setYueCategory(Integer yueCategory) {
        this.yueCategory = yueCategory;
    }

    public Boolean getYueIncomeOut() {
        return yueIncomeOut;
    }

    public void setYueIncomeOut(Boolean yueIncomeOut) {
        this.yueIncomeOut = yueIncomeOut;
    }

    public Float getYueSubtotal() {
        return yueSubtotal;
    }

    public void setYueSubtotal(Float yueSubtotal) {
        this.yueSubtotal = yueSubtotal;
    }

    public Integer getYueStatus() {
        return yueStatus;
    }

    public void setYueStatus(Integer yueStatus) {
        this.yueStatus = yueStatus;
    }

    public Date getYueFoudDate() {
        return yueFoudDate;
    }

    public void setYueFoudDate(Date yueFoudDate) {
        this.yueFoudDate = yueFoudDate;
    }
}