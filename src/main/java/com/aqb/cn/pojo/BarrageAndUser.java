package com.aqb.cn.pojo;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/1/0001.
 */
public class BarrageAndUser {
    private String topicTitle;

    private String id;

    private String userId;

    private String barrageContent;

    private String topicId;

    private Date foundTime;

//    private String id1;


    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    private String userFull;

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

    public String getBarrageContent() {
        return barrageContent;
    }

    public void setBarrageContent(String barrageContent) {
        this.barrageContent = barrageContent;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public Date getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(Date foundTime) {
        this.foundTime = foundTime;
    }

//    public String getId1() {
//        return id1;
//    }
//
//    public void setId1(String id1) {
//        this.id1 = id1;
//    }

    public String getUserFull() {
        return userFull;
    }

    public void setUserFull(String userFull) {
        this.userFull = userFull;
    }
}
