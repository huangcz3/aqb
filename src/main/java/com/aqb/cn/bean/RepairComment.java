package com.aqb.cn.bean;

import java.util.Date;

public class RepairComment {
    private String id;

    private String userId;

    private String repairId;

    private String repairCommentContent;

    private Float repairCommentMoney;

    private Float population;

    private Float technology;

    private Float services;

    private Float environment;

    private String picture1;

    private String picture2;

    private String picture3;

    private Integer status;

    private Date foundDate;

    private User user;//评论用户

    private String reason;//不通过的原因

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

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

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId == null ? null : repairId.trim();
    }

    public String getRepairCommentContent() {
        return repairCommentContent;
    }

    public void setRepairCommentContent(String repairCommentContent) {
        this.repairCommentContent = repairCommentContent == null ? null : repairCommentContent.trim();
    }

    public Float getRepairCommentMoney() {
        return repairCommentMoney;
    }

    public void setRepairCommentMoney(Float repairCommentMoney) {
        this.repairCommentMoney = repairCommentMoney;
    }

    public Float getPopulation() {
        return population;
    }

    public void setPopulation(Float population) {
        this.population = population;
    }

    public Float getTechnology() {
        return technology;
    }

    public void setTechnology(Float technology) {
        this.technology = technology;
    }

    public Float getServices() {
        return services;
    }

    public void setServices(Float services) {
        this.services = services;
    }

    public Float getEnvironment() {
        return environment;
    }

    public void setEnvironment(Float environment) {
        this.environment = environment;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1 == null ? null : picture1.trim();
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2 == null ? null : picture2.trim();
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3 == null ? null : picture3.trim();
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