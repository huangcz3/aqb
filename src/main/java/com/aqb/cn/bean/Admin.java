package com.aqb.cn.bean;

import java.util.Date;

public class Admin {
    private String id;

    private String adminName;

    private String adminPass;

    private String adminNumber;

    private String adminCode1;

    private String adminCode2;

    private String adminCode3;

    private Integer status;

    private Byte authority1;

    private Byte authority2;

    private Byte authority3;

    private Byte authority4;

    private Byte authority5;

    private Byte authority6;

    private Byte authority7;

    private Date foundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName == null ? null : adminName.trim();
    }

    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass == null ? null : adminPass.trim();
    }

    public String getAdminNumber() {
        return adminNumber;
    }

    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber == null ? null : adminNumber.trim();
    }

    public String getAdminCode1() {
        return adminCode1;
    }

    public void setAdminCode1(String adminCode1) {
        this.adminCode1 = adminCode1 == null ? null : adminCode1.trim();
    }

    public String getAdminCode2() {
        return adminCode2;
    }

    public void setAdminCode2(String adminCode2) {
        this.adminCode2 = adminCode2 == null ? null : adminCode2.trim();
    }

    public String getAdminCode3() {
        return adminCode3;
    }

    public void setAdminCode3(String adminCode3) {
        this.adminCode3 = adminCode3 == null ? null : adminCode3.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Byte getAuthority1() {
        return authority1;
    }

    public void setAuthority1(Byte authority1) {
        this.authority1 = authority1;
    }

    public Byte getAuthority2() {
        return authority2;
    }

    public void setAuthority2(Byte authority2) {
        this.authority2 = authority2;
    }

    public Byte getAuthority3() {
        return authority3;
    }

    public void setAuthority3(Byte authority3) {
        this.authority3 = authority3;
    }

    public Byte getAuthority4() {
        return authority4;
    }

    public void setAuthority4(Byte authority4) {
        this.authority4 = authority4;
    }

    public Byte getAuthority5() {
        return authority5;
    }

    public void setAuthority5(Byte authority5) {
        this.authority5 = authority5;
    }

    public Byte getAuthority6() {
        return authority6;
    }

    public void setAuthority6(Byte authority6) {
        this.authority6 = authority6;
    }

    public Byte getAuthority7() {
        return authority7;
    }

    public void setAuthority7(Byte authority7) {
        this.authority7 = authority7;
    }

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Date foundDate) {
        this.foundDate = foundDate;
    }
}