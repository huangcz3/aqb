package com.aqb.cn.bean;

import java.util.Date;

public class Proportion {
    private String id;

    private String proportionName;

    //point 
    private Float proportionFront;

    //money
    private Float proportionAfter;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProportionName() {
        return proportionName;
    }

    public void setProportionName(String proportionName) {
        this.proportionName = proportionName == null ? null : proportionName.trim();
    }

    public Float getProportionFront() {
        return proportionFront;
    }

    public void setProportionFront(Float proportionFront) {
        this.proportionFront = proportionFront;
    }

    public Float getProportionAfter() {
        return proportionAfter;
    }

    public void setProportionAfter(Float proportionAfter) {
        this.proportionAfter = proportionAfter;
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