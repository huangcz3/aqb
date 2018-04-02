package com.aqb.cn.bean;

import java.util.Date;
import java.util.List;

public class User {
    private String id;

    private String userName;

    private String imUserName;

    private String userPass;

    private String userCode1;

    private String userCode2;

    private String userCode3;

    private Integer loginMode;//登录方式

    private String userFull;

    private String userNick;

    private String userSex;

    private String userAge;

    private String userConstellation;

    private String userCard;

    private String userEmail;

    private String userAddress;

    private Dizhi dizhi;

    private String userPay;

    private String userHead;

    private String userCover;

    private String  vipId;//

    private Integer status;

    private Date foundDate;

    private List<Task> tasks;//任务

    private Integer vipGrade;//会员等级

    public Integer getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(Integer vipGrade) {
        this.vipGrade = vipGrade;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Dizhi getDizhi() {
        return dizhi;
    }

    public void setDizhi(Dizhi dizhi) {
        this.dizhi = dizhi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getImUserName() {
        return imUserName;
    }

    public void setImUserName(String imUserName) {
        this.imUserName = imUserName == null ? null : imUserName.trim();
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass == null ? null : userPass.trim();
    }

    public String getUserCode1() {
        return userCode1;
    }

    public void setUserCode1(String userCode1) {
        this.userCode1 = userCode1 == null ? null : userCode1.trim();
    }

    public String getUserCode2() {
        return userCode2;
    }

    public void setUserCode2(String userCode2) {
        this.userCode2 = userCode2 == null ? null : userCode2.trim();
    }

    public String getUserCode3() {
        return userCode3;
    }

    public void setUserCode3(String userCode3) {
        this.userCode3 = userCode3 == null ? null : userCode3.trim();
    }

    public Integer getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(Integer loginMode) {
        this.loginMode = loginMode;
    }

    public String getUserFull() {
        return userFull;
    }

    public void setUserFull(String userFull) {
        this.userFull = userFull == null ? null : userFull.trim();
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick == null ? null : userNick.trim();
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex == null ? null : userSex.trim();
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge == null ? null : userAge.trim();
    }

    public String getUserConstellation() {
        return userConstellation;
    }

    public void setUserConstellation(String userConstellation) {
        this.userConstellation = userConstellation == null ? null : userConstellation.trim();
    }

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard == null ? null : userCard.trim();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    public String getUserPay() {
        return userPay;
    }

    public void setUserPay(String userPay) {
        this.userPay = userPay == null ? null : userPay.trim();
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead == null ? null : userHead.trim();
    }

    public String getUserCover() {
        return userCover;
    }

    public void setUserCover(String userCover) {
        this.userCover = userCover == null ? null : userCover.trim();
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

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", userCode1='" + userCode1 + '\'' +
                ", userCode2='" + userCode2 + '\'' +
                ", userCode3='" + userCode3 + '\'' +
                ", loginMode=" + loginMode +
                ", userFull='" + userFull + '\'' +
                ", userNick='" + userNick + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userAge='" + userAge + '\'' +
                ", userConstellation='" + userConstellation + '\'' +
                ", userCard='" + userCard + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", dizhi=" + dizhi +
                ", userPay='" + userPay + '\'' +
                ", userHead='" + userHead + '\'' +
                ", userCover='" + userCover + '\'' +
                ", vipId='" + vipId + '\'' +
                ", status=" + status +
                ", foundDate=" + foundDate +
                ", tasks=" + tasks +
                '}';
    }
}