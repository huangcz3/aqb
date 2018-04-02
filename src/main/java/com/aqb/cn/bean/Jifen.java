package com.aqb.cn.bean;

import java.util.Date;

public class Jifen {
    private String id;

    private String userId;

    private Integer jifenCategory;

    private Boolean jifenIncomeOut;

    private Float jifenSubtotal;

    private Integer jifenStatus;

    private Date jifenFoudDate;

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

    public Integer getJifenCategory() {
        return jifenCategory;
    }

    public void setJifenCategory(Integer jifenCategory) {
        this.jifenCategory = jifenCategory;
    }

    public Boolean getJifenIncomeOut() {
        return jifenIncomeOut;
    }

    public void setJifenIncomeOut(Boolean jifenIncomeOut) {
        this.jifenIncomeOut = jifenIncomeOut;
    }

    public Float getJifenSubtotal() {
        return jifenSubtotal;
    }

    public void setJifenSubtotal(Float jifenSubtotal) {
        this.jifenSubtotal = jifenSubtotal;
    }

    public Integer getJifenStatus() {
        return jifenStatus;
    }

    public void setJifenStatus(Integer jifenStatus) {
        this.jifenStatus = jifenStatus;
    }

    public Date getJifenFoudDate() {
        return jifenFoudDate;
    }

    public void setJifenFoudDate(Date jifenFoudDate) {
        this.jifenFoudDate = jifenFoudDate;
    }
}