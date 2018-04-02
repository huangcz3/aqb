package com.aqb.cn.pojo;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/4/0004.
 */
public class RcAndUser {
    private String id;//修车圈点评表id

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

    private String userName;//用户手机号

    private String repairName;//店铺名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getRepairCommentContent() {
        return repairCommentContent;
    }

    public void setRepairCommentContent(String repairCommentContent) {
        this.repairCommentContent = repairCommentContent;
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
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
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

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }
}
