package com.aqb.cn.bean;

import java.util.Date;

public class UserVip {
    private String id;

    private String userId;

    private String vipId;

    private Date openTime;

    private Integer lengthTime;

    private Integer surplusDays;

    private Date startTime;

    private Integer vipGrade;

    private Date stopTime;

    private Integer status;

    private Date foundDate;

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

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId == null ? null : vipId.trim();
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Integer getLengthTime() {
        return lengthTime;
    }

    public void setLengthTime(Integer lengthTime) {
        this.lengthTime = lengthTime;
    }

    public Integer getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(Integer surplusDays) {
        this.surplusDays = surplusDays;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(Integer vipGrade) {
        this.vipGrade = vipGrade;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
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