package com.aqb.cn.bean;

import java.util.Date;

public class DeliveryPlatform {
    private String id;

    private String userId;

    private String adverPlatformId;

    private String adverPlatformContent;

    private Double longitude;

    private Double latitude;

    private Float adverPlatformMoney;

    private Float adverPlatformScore;

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

    public String getAdverPlatformId() {
        return adverPlatformId;
    }

    public void setAdverPlatformId(String adverPlatformId) {
        this.adverPlatformId = adverPlatformId == null ? null : adverPlatformId.trim();
    }

    public String getAdverPlatformContent() {
        return adverPlatformContent;
    }

    public void setAdverPlatformContent(String adverPlatformContent) {
        this.adverPlatformContent = adverPlatformContent == null ? null : adverPlatformContent.trim();
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Float getAdverPlatformMoney() {
        return adverPlatformMoney;
    }

    public void setAdverPlatformMoney(Float adverPlatformMoney) {
        this.adverPlatformMoney = adverPlatformMoney;
    }

    public Float getAdverPlatformScore() {
        return adverPlatformScore;
    }

    public void setAdverPlatformScore(Float adverPlatformScore) {
        this.adverPlatformScore = adverPlatformScore;
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