package com.aqb.cn.bean;

import java.util.Date;

public class Daxiaoquan {
    private String id;

    private String daxiaoquanName;

    private Float daxiaoquanCanshu;

    private String daxiaoquanYanse;

    private Integer xulie;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDaxiaoquanName() {
        return daxiaoquanName;
    }

    public void setDaxiaoquanName(String daxiaoquanName) {
        this.daxiaoquanName = daxiaoquanName == null ? null : daxiaoquanName.trim();
    }

    public Float getDaxiaoquanCanshu() {
        return daxiaoquanCanshu;
    }

    public void setDaxiaoquanCanshu(Float daxiaoquanCanshu) {
        this.daxiaoquanCanshu = daxiaoquanCanshu;
    }

    public String getDaxiaoquanYanse() {
        return daxiaoquanYanse;
    }

    public void setDaxiaoquanYanse(String daxiaoquanYanse) {
        this.daxiaoquanYanse = daxiaoquanYanse == null ? null : daxiaoquanYanse.trim();
    }

    public Integer getXulie() {
        return xulie;
    }

    public void setXulie(Integer xulie) {
        this.xulie = xulie;
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