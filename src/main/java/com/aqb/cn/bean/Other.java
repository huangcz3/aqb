package com.aqb.cn.bean;

import java.util.Date;

public class Other {
    private String id;

    private String otherName;

    private Float otherCanshu;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName == null ? null : otherName.trim();
    }

    public Float getOtherCanshu() {
        return otherCanshu;
    }

    public void setOtherCanshu(Float otherCanshu) {
        this.otherCanshu = otherCanshu;
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