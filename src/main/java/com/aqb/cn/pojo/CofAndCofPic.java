package com.aqb.cn.pojo;

import com.aqb.cn.bean.Cof;
import com.aqb.cn.bean.CofPic;
import com.aqb.cn.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */
public class CofAndCofPic {
    private Cof cof;

    List<CofPic> cofPics;

    private User user;

    public Cof getCof() {
        return cof;
    }

    public void setCof(Cof cof) {
        this.cof = cof;
    }

    public List<CofPic> getCofPics() {
        return cofPics;
    }

    public void setCofPics(List<CofPic> cofPics) {
        this.cofPics = cofPics;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
