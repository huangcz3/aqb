package com.aqb.cn.bean;

import java.util.Date;

public class Redpacket {
    private String id;

    private String userId;

    private Integer redCategory;

    private Boolean redIncomeOut;

    private Float redSubtotal;

    private Integer redStatus;

    private Date redFoundDate;

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

    public Integer getRedCategory() {
        return redCategory;
    }

    public void setRedCategory(Integer redCategory) {
        this.redCategory = redCategory;
    }

    public Boolean getRedIncomeOut() {
        return redIncomeOut;
    }

    public void setRedIncomeOut(Boolean redIncomeOut) {
        this.redIncomeOut = redIncomeOut;
    }

    public Float getRedSubtotal() {
        return redSubtotal;
    }

    public void setRedSubtotal(Float redSubtotal) {
        this.redSubtotal = redSubtotal;
    }

    public Integer getRedStatus() {
        return redStatus;
    }

    public void setRedStatus(Integer redStatus) {
        this.redStatus = redStatus;
    }

    public Date getRedFoundDate() {
        return redFoundDate;
    }

    public void setRedFoundDate(Date redFoundDate) {
        this.redFoundDate = redFoundDate;
    }
}