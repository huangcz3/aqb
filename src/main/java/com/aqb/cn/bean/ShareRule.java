package com.aqb.cn.bean;

import java.sql.Timestamp;

public class ShareRule {
  private Long shareSetId;  //ID
  private Long integral;  //奖励的积分
  private Double money;  //奖励的金额
  private Long shareRule;  //分享规则，1为积分，2为钱
  private String adminId;  //操作员ID
  private java.sql.Timestamp time;  //操作时间
  private Long state;  //状态

  public ShareRule() {
  }

  @Override
  public String toString() {
    return "ShareRule{" +
            "shareSetId=" + shareSetId +
            ", integral=" + integral +
            ", money=" + money +
            ", shareRule=" + shareRule +
            ", adminId='" + adminId + '\'' +
            ", time=" + time +
            ", state=" + state +
            '}';
  }

  public void setShareSetId(Long shareSetId) {
    this.shareSetId = shareSetId;
  }

  public void setIntegral(Long integral) {
    this.integral = integral;
  }

  public void setMoney(Double money) {
    this.money = money;
  }

  public void setShareRule(Long shareRule) {
    this.shareRule = shareRule;
  }


  public void setAdminId(String adminId) {
    this.adminId = adminId;
  }

  public void setTime(Timestamp time) {
    this.time = time;
  }

  public void setState(Long state) {
    this.state = state;
  }

  public Long getShareSetId() {
    return shareSetId;
  }

  public Long getIntegral() {
    return integral;
  }

  public Double getMoney() {
    return money;
  }

  public Long getShareRule() {
    return shareRule;
  }


  public String getAdminId() {
    return adminId;
  }

  public Timestamp getTime() {
    return time;
  }

  public Long getState() {
    return state;
  }

  public ShareRule(Long shareSetId, Long integral, Double money, Long shareRule, String adminId, Timestamp time, Long state) {
    this.shareSetId = shareSetId;
    this.integral = integral;
    this.money = money;
    this.shareRule = shareRule;
    this.adminId = adminId;
    this.time = time;
    this.state = state;
  }
}
