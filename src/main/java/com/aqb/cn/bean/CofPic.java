package com.aqb.cn.bean;

import java.util.Date;

public class CofPic {
    private String id;

    private String cofId;

    private String cofPicAddress;

    private String cofPicSaddress;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCofId() {
        return cofId;
    }

    public void setCofId(String cofId) {
        this.cofId = cofId == null ? null : cofId.trim();
    }

    public String getCofPicAddress() {
        return cofPicAddress;
    }

    public void setCofPicAddress(String cofPicAddress) {
        this.cofPicAddress = cofPicAddress == null ? null : cofPicAddress.trim();
    }

    public String getCofPicSaddress() {
        return cofPicSaddress;
    }

    public void setCofPicSaddress(String cofPicSaddress) {
        this.cofPicSaddress = cofPicSaddress == null ? null : cofPicSaddress.trim();
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