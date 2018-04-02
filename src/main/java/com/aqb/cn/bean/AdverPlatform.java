package com.aqb.cn.bean;

import java.util.Date;

public class AdverPlatform {
    private String id;

    private String city;

    private String adver;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getAdver() {
        return adver;
    }

    public void setAdver(String adver) {
        this.adver = adver == null ? null : adver.trim();
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