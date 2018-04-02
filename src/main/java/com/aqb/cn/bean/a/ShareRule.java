package com.aqb.cn.bean.a;

import java.math.BigDecimal;
import java.util.Date;

public class ShareRule {
    private Integer shareSetId;

    private Integer integral;

    private BigDecimal money;

    private Integer shareRule;

    private String adminId;

    private Date time;

    private Integer state;

    public Integer getShareSetId() {
        return shareSetId;
    }

    public void setShareSetId(Integer shareSetId) {
        this.shareSetId = shareSetId;
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

    public Integer getShareRule() {
        return shareRule;
    }

    public void setShareRule(Integer shareRule) {
        this.shareRule = shareRule;
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