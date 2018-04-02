package com.aqb.cn.bean;

import java.util.Date;

public class Shield {
    private String id;

    private String shieldGuanjianci;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getShieldGuanjianci() {
        return shieldGuanjianci;
    }

    public void setShieldGuanjianci(String shieldGuanjianci) {
        this.shieldGuanjianci = shieldGuanjianci == null ? null : shieldGuanjianci.trim();
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