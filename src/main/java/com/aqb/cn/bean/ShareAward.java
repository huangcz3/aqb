package com.aqb.cn.bean;


import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;

public class ShareAward {
  private Long shareAwardId;  //ID
  private String userId;  //分享人的ID
  private String shareTel;  //接收分享的人的手机号
  private Long integral;  //获得的积分
  private Double money;  //获得的金额
  private java.sql.Timestamp shareTime;  //分享成功的时间
  private Long rankingState;  //排行状态，1为参与排行，0为不参与
  private Long awardState;  //奖励状态，1为已奖励，0为未奖励
  private String adminId;  //操作员ID
  private Date time;  //操作时间
  private Long state;  //状态

  public ShareAward() {
  }

  @Override
  public String toString() {
    return "ShareAward{" +
            "shareAwardId=" + shareAwardId +
            ", userId='" + userId + '\'' +
            ", shareTel='" + shareTel + '\'' +
            ", integral=" + integral +
            ", money=" + money +
            ", shareTime=" + shareTime +
            ", rankingState=" + rankingState +
            ", awardState=" + awardState +
            ", adminId='" + adminId + '\'' +
            ", time=" + time +
            ", state=" + state +
            '}';
  }

  public void setShareAwardId(Long shareAwardId) {
    this.shareAwardId = shareAwardId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setShareTel(String shareTel) {
    this.shareTel = shareTel;
  }

  public void setIntegral(Long integral) {
    this.integral = integral;
  }

  public void setMoney(Double money) {
    this.money = money;
  }

  public void setShareTime(Timestamp shareTime) {
    this.shareTime = shareTime;
  }

  public void setRankingState(Long rankingState) {
    this.rankingState = rankingState;
  }

  public void setAwardState(Long awardState) {
    this.awardState = awardState;
  }

  public void setAdminId(String adminId) {
    this.adminId = adminId;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public void setState(Long state) {
    this.state = state;
  }

  public Long getShareAwardId() {
    return shareAwardId;
  }

  public String getUserId() {
    return userId;
  }

  public String getShareTel() {
    return shareTel;
  }

  public Long getIntegral() {
    return integral;
  }

  public Double getMoney() {
    return money;
  }

  public Timestamp getShareTime() {
    return shareTime;
  }

  public Long getRankingState() {
    return rankingState;
  }

  public Long getAwardState() {
    return awardState;
  }

  public String getAdminId() {
    return adminId;
  }

  public Date getTime() {
    return time;
  }

  public Long getState() {
    return state;
  }

  public ShareAward(Long shareAwardId, String userId, String shareTel, Long integral, Double money, Timestamp shareTime, Long rankingState, Long awardState, String adminId, Date time, Long state) {
    this.shareAwardId = shareAwardId;
    this.userId = userId;
    this.shareTel = shareTel;
    this.integral = integral;
    this.money = money;
    this.shareTime = shareTime;
    this.rankingState = rankingState;
    this.awardState = awardState;
    this.adminId = adminId;
    this.time = time;
    this.state = state;
  }
}
