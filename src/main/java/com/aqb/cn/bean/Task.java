package com.aqb.cn.bean;

import java.util.Date;

public class Task {
    private String id;

    private String userId;

    private String taskName;

    private String taskDetails;

    private Integer taskReward;

    private Integer status;

    private Date foundDate;

    private String url;//存放任务访问地址

    public Task(){
    }

    public Task(String id, String userId, String taskName, String taskDetails, Integer taskReward, Integer status, Date foundDate, String url) {
        this.id = id;
        this.userId = userId;
        this.taskName = taskName;
        this.taskDetails = taskDetails;
        this.taskReward = taskReward;
        this.status = status;
        this.foundDate = foundDate;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails == null ? null : taskDetails.trim();
    }

    public Integer getTaskReward() {
        return taskReward;
    }

    public void setTaskReward(Integer taskReward) {
        this.taskReward = taskReward;
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