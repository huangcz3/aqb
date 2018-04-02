package com.aqb.cn.bean;

import java.util.Date;

public class Divided {
    private String id;

    private String dividedName;

    private Float dividedBili;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDividedName() {
        return dividedName;
    }

    public void setDividedName(String dividedName) {
        this.dividedName = dividedName == null ? null : dividedName.trim();
    }

    public Float getDividedBili() {
        return dividedBili;
    }

    public void setDividedBili(Float dividedBili) {
        this.dividedBili = dividedBili;
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