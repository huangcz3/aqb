package com.aqb.cn.pojo;

import com.aqb.cn.bean.*;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
public class CofAndCofCommentAndCofFabulousAndCofPic {
    private Cof cof;

    private List<CofComment> cofComments;

    private List<CofFabulous> cofFabulouses;

    private List<CofPic> cofPics;

    private User user;

    public List<CofComment> getCofComments() {
        return cofComments;
    }

    public void setCofComments(List<CofComment> cofComments) {
        this.cofComments = cofComments;
    }

    public Cof getCof() {
        return cof;
    }

    public void setCof(Cof cof) {
        this.cof = cof;
    }

    public List<CofFabulous> getCofFabulouses() {
        return cofFabulouses;
    }

    public void setCofFabulouses(List<CofFabulous> cofFabulouses) {
        this.cofFabulouses = cofFabulouses;
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
