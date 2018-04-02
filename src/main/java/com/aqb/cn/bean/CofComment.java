package com.aqb.cn.bean;

import java.util.Date;

public class CofComment {
    private String id;

    private String cofId;

    private String userId;

    private String cofCommentContent;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCofId() {
        return cofId;
    }

    public void setCofId(String cofId) {
        this.cofId = cofId == null ? null : cofId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getCofCommentContent() {
        return cofCommentContent;
    }

    public void setCofCommentContent(String cofCommentContent) {
        this.cofCommentContent = cofCommentContent == null ? null : cofCommentContent.trim();
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