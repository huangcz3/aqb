package com.aqb.cn.pojo;

/**
 * Created by Administrator on 2017/7/17.
 */
public class Alipay {

    private String boby;//描述

    private String subject;//名称

    private String outTradeNo;//订单号

    private String totalAmount;//订单金额

    private Integer status;//状态（1--充值，2--押金）

    private Integer total_fee;//微信的订单金额


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getBoby() {
        return boby;
    }

    public void setBoby(String boby) {
        this.boby = boby;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
