package com.aqb.cn.bean;

import java.sql.Timestamp;

public class ShareComment {
  private Long shareCommentId;  //ID
  private String rule;  //规则说明
  private String adminId;  //操作员ID
  private java.sql.Timestamp time;  //操作时间
  private Long state;  //状态

  public ShareComment() {
  }

  @Override
  public String toString() {
    return "ShareComment{" +
            "shareCommentId=" + shareCommentId +
            ", rule='" + rule + '\'' +
            ", adminId='" + adminId + '\'' +
            ", time=" + time +
            ", state=" + state +
            '}';
  }

  public void setShareCommentId(Long shareCommentId) {
    this.shareCommentId = shareCommentId;
  }

  public void setRule(String rule) {
    this.rule = rule;
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

  public Long getShareCommentId() {
    return shareCommentId;
  }

  public String getRule() {
    return rule;
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

  public ShareComment(Long shareCommentId, String rule, String adminId, Timestamp time, Long state) {
    this.shareCommentId = shareCommentId;
    this.rule = rule;
    this.adminId = adminId;
    this.time = time;
    this.state = state;
  }
}
