package com.aqb.cn.pojo;

import com.aqb.cn.bean.User;

/**
 * Created by 黄成钊 on 2017/7/27.
 */
public class Usersearch {
    private User user;

    private Integer binding_status;//绑定情况（设备安装情况）

    private Integer deposit_status;//（押金情况）

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
