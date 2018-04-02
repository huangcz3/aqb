package com.aqb.cn.bean;

import java.util.Date;

public class Wallet {
    private String id;

    private String userId;

    private String yueId;

    private Float yue;

    private String jifenId;

    private Integer jifen;

    private String kajuanId;

    private Integer kajuan;

    private String baoixanId;

    private Integer baoxian;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getYueId() {
        return yueId;
    }

    public void setYueId(String yueId) {
        this.yueId = yueId == null ? null : yueId.trim();
    }

    public Float getYue() {
        return yue;
    }

    public void setYue(Float yue) {
        this.yue = yue;
    }

    public String getJifenId() {
        return jifenId;
    }

    public void setJifenId(String jifenId) {
        this.jifenId = jifenId == null ? null : jifenId.trim();
    }

    public Integer getJifen() {
        return jifen;
    }

    public void setJifen(Integer jifen) {
        this.jifen = jifen;
    }

    public String getKajuanId() {
        return kajuanId;
    }

    public void setKajuanId(String kajuanId) {
        this.kajuanId = kajuanId == null ? null : kajuanId.trim();
    }

    public Integer getKajuan() {
        return kajuan;
    }

    public void setKajuan(Integer kajuan) {
        this.kajuan = kajuan;
    }

    public String getBaoixanId() {
        return baoixanId;
    }

    public void setBaoixanId(String baoixanId) {
        this.baoixanId = baoixanId == null ? null : baoixanId.trim();
    }

    public Integer getBaoxian() {
        return baoxian;
    }

    public void setBaoxian(Integer baoxian) {
        this.baoxian = baoxian;
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