package com.aqb.cn.pojo;

import com.aqb.cn.bean.Dizhi;

/**
 * Created by Administrator on 2017/7/27/0027.
 */
public class DepositAndUser {
    private String userName;//手机号

    private String userFull;//用户真实姓名

    private String address;//用户地址

    private Dizhi dizhi;

    private Integer binding_status;//绑定情况（设备安装情况）

    private Integer deposit_status;//（押金情况）

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFull() {
        return userFull;
    }

    public void setUserFull(String userFull) {
        this.userFull = userFull;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getBinding_status() {
        return binding_status;
    }

    public void setBinding_status(Integer binding_status) {
        this.binding_status = binding_status;
    }

    public Integer getDeposit_status() {
        return deposit_status;
    }

    public void setDeposit_status(Integer deposit_status) {
        this.deposit_status = deposit_status;
    }

    public Dizhi getDizhi() {
        return dizhi;
    }

    public void setDizhi(Dizhi dizhi) {
        this.dizhi = dizhi;
    }
}
