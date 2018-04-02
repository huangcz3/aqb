package com.aqb.cn.bean;

import java.util.Date;

public class ShareReward {
    private String id;

    private String userId;

    private String rewardContent;

    private Float rewardPercent;

    private String rewardContent2;

    private Float rewardPercent2;

    private Integer status;

    private Date foundDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRewardContent2() {
        return rewardContent2;
    }

    public void setRewardContent2(String rewardContent2) {
        this.rewardContent2 = rewardContent2;
    }

    public Float getRewardPercent2() {
        return rewardPercent2;
    }

    public void setRewardPercent2(Float rewardPercent2) {
        this.rewardPercent2 = rewardPercent2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRewardContent() {
        return rewardContent;
    }

    public void setRewardContent(String rewardContent) {
        this.rewardContent = rewardContent == null ? null : rewardContent.trim();
    }

    public Float getRewardPercent() {
        return rewardPercent;
    }

    public void setRewardPercent(Float rewardPercent) {
        this.rewardPercent = rewardPercent;
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