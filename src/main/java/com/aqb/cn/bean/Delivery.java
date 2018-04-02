package com.aqb.cn.bean;

import java.util.Date;

public class Delivery {
    private String id;

    private String userId;

    private String advertisementId;

    private Float deliveryMoney;

    private Integer redPacketStatus;

    private Integer redPacketType;

    private Float redPacketMoney;

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

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId == null ? null : advertisementId.trim();
    }

    public Float getDeliveryMoney() {
        return deliveryMoney;
    }

    public void setDeliveryMoney(Float deliveryMoney) {
        this.deliveryMoney = deliveryMoney;
    }

    public Integer getRedPacketStatus() {
        return redPacketStatus;
    }

    public void setRedPacketStatus(Integer redPacketStatus) {
        this.redPacketStatus = redPacketStatus;
    }

    public Integer getRedPacketType() {
        return redPacketType;
    }

    public void setRedPacketType(Integer redPacketType) {
        this.redPacketType = redPacketType;
    }

    public Float getRedPacketMoney() {
        return redPacketMoney;
    }

    public void setRedPacketMoney(Float redPacketMoney) {
        this.redPacketMoney = redPacketMoney;
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