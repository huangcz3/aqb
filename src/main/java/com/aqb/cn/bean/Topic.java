package com.aqb.cn.bean;

import java.util.Date;

public class Topic {
    private String id;

    private String topicTitle;

    private String topicContent;

    private String topicYuliu1;

    private String topicYuliu2;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle == null ? null : topicTitle.trim();
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent == null ? null : topicContent.trim();
    }

    public String getTopicYuliu1() {
        return topicYuliu1;
    }

    public void setTopicYuliu1(String topicYuliu1) {
        this.topicYuliu1 = topicYuliu1 == null ? null : topicYuliu1.trim();
    }

    public String getTopicYuliu2() {
        return topicYuliu2;
    }

    public void setTopicYuliu2(String topicYuliu2) {
        this.topicYuliu2 = topicYuliu2 == null ? null : topicYuliu2.trim();
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