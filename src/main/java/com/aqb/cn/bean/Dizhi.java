package com.aqb.cn.bean;

import java.util.Date;

public class Dizhi {
    private String id;

    private String dizhiName;

    private String dizhiPhone;

    private String dizhiDiqu;

    private String dizhiJiedao;

    private String dizhiXiangxi;

    private String userId;

    private Integer status;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDizhiName() {
        return dizhiName;
    }

    public void setDizhiName(String dizhiName) {
        this.dizhiName = dizhiName == null ? null : dizhiName.trim();
    }

    public String getDizhiPhone() {
        return dizhiPhone;
    }

    public void setDizhiPhone(String dizhiPhone) {
        this.dizhiPhone = dizhiPhone == null ? null : dizhiPhone.trim();
    }

    public String getDizhiDiqu() {
        return dizhiDiqu;
    }

    public void setDizhiDiqu(String dizhiDiqu) {
        this.dizhiDiqu = dizhiDiqu == null ? null : dizhiDiqu.trim();
    }

    public String getDizhiJiedao() {
        return dizhiJiedao;
    }

    public void setDizhiJiedao(String dizhiJiedao) {
        this.dizhiJiedao = dizhiJiedao == null ? null : dizhiJiedao.trim();
    }

    public String getDizhiXiangxi() {
        return dizhiXiangxi;
    }

    public void setDizhiXiangxi(String dizhiXiangxi) {
        this.dizhiXiangxi = dizhiXiangxi == null ? null : dizhiXiangxi.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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