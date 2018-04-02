package com.aqb.cn.bean;

import java.util.Date;

public class Sign {
    private String id;

    private String userId;

    private Integer status;

    private Date foundDate;

    private String week;//星期

    private String dateSeven;//时间

    private Boolean sin;//是否签到

    public Boolean getSin() {
        return sin;
    }

    public void setSin(Boolean sin) {
        this.sin = sin;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDateSeven() {
        return dateSeven;
    }

    public void setDateSeven(String dateSeven) {
        this.dateSeven = dateSeven;
    }

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