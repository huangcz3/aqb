package com.aqb.cn.pojo;

/**
 * Created by Administrator on 2017/7/27/0027.
 */
public class BindingContent {
    private  String carInfo;//车辆信息

    private String bindingNumber;//绑定设备号

    private String userFull;//用户真实姓名

    private String userCard;//用户身份证号码

    private Integer auditStatus;//绑定审核状态 状态（0--审核不通过，1--审核通过，2-审核中,3-已解绑，4-解绑审核中，5-解绑审核不通过）

    private Integer depositStatus;//押金缴纳状态

    private String reason;//未通过的原因

    private String relieve;//解绑原因

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public String getBindingNumber() {
        return bindingNumber;
    }

    public void setBindingNumber(String bindingNumber) {
        this.bindingNumber = bindingNumber;
    }

    public String getUserFull() {
        return userFull;
    }

    public void setUserFull(String userFull) {
        this.userFull = userFull;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public Integer getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(Integer depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRelieve() {
        return relieve;
    }

    public void setRelieve(String relieve) {
        this.relieve = relieve;
    }
}
