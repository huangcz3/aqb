package com.aqb.cn.pojo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2/0002.
 */
public class TopicAndBarrage {

    private String topicTitle;

    private List<BarrageAndUser> barrageAndUsers;

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public List<BarrageAndUser> getBarrageAndUsers() {
        return barrageAndUsers;
    }

    public void setBarrageAndUsers(List<BarrageAndUser> barrageAndUsers) {
        this.barrageAndUsers = barrageAndUsers;
    }
}
