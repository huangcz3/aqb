package com.aqb.cn.bean;

import java.util.Date;

public class Barrage {
    private String id;

    private String userId;

    private String barrageContent;

    private Integer barrageType;

    private String topicId;

    private Integer barrageColor;

    private Integer bLike;

    private boolean bLike_status;//点赞的状态，是否能点赞，true-可以点赞，false-不能点赞

    private Integer bUnlike;

    private boolean bUnlike_status;//点差的状态，是否能点差，true-可以点赞，false-不能点差

    private Date foundTime;

    private Integer status;

    private Date foundDate;

    public boolean isbUnlike_status() {
        return bUnlike_status;
    }

    public void setbUnlike_status(boolean bUnlike_status) {
        this.bUnlike_status = bUnlike_status;
    }

    public boolean isbLike_status() {
        return bLike_status;
    }

    public void setbLike_status(boolean bLike_status) {
        this.bLike_status = bLike_status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

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

    public String getBarrageContent() {
        return barrageContent;
    }

    public void setBarrageContent(String barrageContent) {
        this.barrageContent = barrageContent == null ? null : barrageContent.trim();
    }

    public Integer getBarrageType() {
        return barrageType;
    }

    public void setBarrageType(Integer barrageType) {
        this.barrageType = barrageType;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId == null ? null : topicId.trim();
    }

    public Integer getBarrageColor() {
        return barrageColor;
    }

    public void setBarrageColor(Integer barrageColor) {
        this.barrageColor = barrageColor;
    }

    public Integer getbLike() {
        return bLike;
    }

    public void setbLike(Integer bLike) {
        this.bLike = bLike;
    }

    public Integer getbUnlike() {
        return bUnlike;
    }

    public void setbUnlike(Integer bUnlike) {
        this.bUnlike = bUnlike;
    }

    public Date getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(Date foundTime) {
        this.foundTime = foundTime;
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