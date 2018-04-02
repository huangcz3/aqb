package com.aqb.cn.bean;

import java.util.Date;

public class ProfitUser {
    private String id;

    private String advertisementId;

    private String equipmentNumber;

    private String userId;

    private Integer tjrtype;

    private Float money;

    private Float money1;

    private Float money2;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId == null ? null : advertisementId.trim();
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber == null ? null : equipmentNumber.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getTjrtype() {
        return tjrtype;
    }

    public void setTjrtype(Integer tjrtype) {
        this.tjrtype = tjrtype;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Float getMoney1() {
        return money1;
    }

    public void setMoney1(Float money1) {
        this.money1 = money1;
    }

    public Float getMoney2() {
        return money2;
    }

    public void setMoney2(Float money2) {
        this.money2 = money2;
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