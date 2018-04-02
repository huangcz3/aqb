package com.aqb.cn.bean;

import java.util.Date;

public class Withdrawals {
    private String id;

    private String userId;

    private Float withdrawalsMoney;

    private Integer accountNumberType;

    private String accountNumber;

    private String adminId;

    private String reason;

    private Date operationDate;

    private Integer status;

    private Date foundDate;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public Float getWithdrawalsMoney() {
        return withdrawalsMoney;
    }

    public void setWithdrawalsMoney(Float withdrawalsMoney) {
        this.withdrawalsMoney = withdrawalsMoney;
    }

    public Integer getAccountNumberType() {
        return accountNumberType;
    }

    public void setAccountNumberType(Integer accountNumberType) {
        this.accountNumberType = accountNumberType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber == null ? null : accountNumber.trim();
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId == null ? null : adminId.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
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