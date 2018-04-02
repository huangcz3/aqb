package com.aqb.cn.bean;

import java.util.Date;

public class BarrageUnlike {
    private String id;

    private String barrageId;

    private String userId;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBarrageId() {
        return barrageId;
    }

    public void setBarrageId(String barrageId) {
        this.barrageId = barrageId == null ? null : barrageId.trim();
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