package com.aqb.cn.pojo;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/3/0003.
 */
public class MyVip {
    private Integer vipGrade;

    private Date expirationDate;

    private Integer surplusDays;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(Integer vipGrade) {
        this.vipGrade = vipGrade;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(Integer surplusDays) {
        this.surplusDays = surplusDays;
    }
}
