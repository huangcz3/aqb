package com.aqb.cn.bean;

import java.util.Date;

public class Binding {
    private String id;

    private String bindingNumber;

    private String bindingCar;

    private String userId;

    private String bindingFull;

    private String bindingIdcard;

    private String bindingTravel;

    private String idcardFront;

    private String idcardBack;

    private String travelFront;

    private String travelBack;

    private String reason;

    private String relieve;

    private Integer depositStatus;

    private Integer status;

    private Date foundDate;

    private String userName;//手机号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBindingNumber() {
        return bindingNumber;
    }

    public void setBindingNumber(String bindingNumber) {
        this.bindingNumber = bindingNumber == null ? null : bindingNumber.trim();
    }

    public String getBindingCar() {
        return bindingCar;
    }

    public void setBindingCar(String bindingCar) {
        this.bindingCar = bindingCar == null ? null : bindingCar.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getBindingFull() {
        return bindingFull;
    }

    public void setBindingFull(String bindingFull) {
        this.bindingFull = bindingFull == null ? null : bindingFull.trim();
    }

    public String getBindingIdcard() {
        return bindingIdcard;
    }

    public void setBindingIdcard(String bindingIdcard) {
        this.bindingIdcard = bindingIdcard == null ? null : bindingIdcard.trim();
    }

    public String getBindingTravel() {
        return bindingTravel;
    }

    public void setBindingTravel(String bindingTravel) {
        this.bindingTravel = bindingTravel == null ? null : bindingTravel.trim();
    }

    public String getIdcardFront() {
        return idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront == null ? null : idcardFront.trim();
    }

    public String getIdcardBack() {
        return idcardBack;
    }

    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack == null ? null : idcardBack.trim();
    }

    public String getTravelFront() {
        return travelFront;
    }

    public void setTravelFront(String travelFront) {
        this.travelFront = travelFront == null ? null : travelFront.trim();
    }

    public String getTravelBack() {
        return travelBack;
    }

    public void setTravelBack(String travelBack) {
        this.travelBack = travelBack == null ? null : travelBack.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Integer getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(Integer depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getRelieve() {
        return relieve;
    }

    public void setRelieve(String relieve) {
        this.relieve = relieve == null ? null : relieve.trim();
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}