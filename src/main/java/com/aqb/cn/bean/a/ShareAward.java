package com.aqb.cn.bean.a;

import java.math.BigDecimal;
import java.util.Date;

public class ShareAward {
    private Integer shareAwardId;

    private String userId;

    private String shareTel;

    private Integer integral;

    private BigDecimal money;

    private Date shareTime;

    private Integer rankingState;

    private Integer awardState;

    private String adminId;

    private Date time;

    private Integer state;

    public Integer getShareAwardId() {
        return shareAwardId;
    }

    public void setShareAwardId(Integer shareAwardId) {
        this.shareAwardId = shareAwardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getShareTel() {
        return shareTel;
    }

    public void setShareTel(String shareTel) {
        this.shareTel = shareTel == null ? null : shareTel.trim();
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }

    public Integer getRankingState() {
        return rankingState;
    }

    public void setRankingState(Integer rankingState) {
        this.rankingState = rankingState;
    }

    public Integer getAwardState() {
        return awardState;
    }

    public void setAwardState(Integer awardState) {
        this.awardState = awardState;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId == null ? null : adminId.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}