package com.aqb.cn.bean;

import java.util.Date;

public class BindingNumber {
    private String id;

    private String batchNumber;

    private String batchDescribe;

    private String deviceNumber;//设备号

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber == null ? null : batchNumber.trim();
    }

    public String getBatchDescribe() {
        return batchDescribe;
    }

    public void setBatchDescribe(String batchDescribe) {
        this.batchDescribe = batchDescribe == null ? null : batchDescribe.trim();
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber == null ? null : deviceNumber.trim();
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