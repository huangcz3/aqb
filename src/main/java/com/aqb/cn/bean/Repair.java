package com.aqb.cn.bean;

import java.util.Date;
import java.util.List;

public class Repair {
    private String id;

    private String repairName;

    private String repairIntroduce;

    private String repairCover;

    private Integer repairPopularity;

    private Double repairLongitude;

    private Double repairLatitude;

    private String repairAddress;

    private String repairPhone;

    private Integer status;

    private Date foundDate;

    private double distance;//距离

    private List<RepairComment> repairComments;//评论

    public List<RepairComment> getRepairComments() {
        return repairComments;
    }

    public void setRepairComments(List<RepairComment> repairComments) {
        this.repairComments = repairComments;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName == null ? null : repairName.trim();
    }

    public String getRepairIntroduce() {
        return repairIntroduce;
    }

    public void setRepairIntroduce(String repairIntroduce) {
        this.repairIntroduce = repairIntroduce == null ? null : repairIntroduce.trim();
    }

    public String getRepairCover() {
        return repairCover;
    }

    public void setRepairCover(String repairCover) {
        this.repairCover = repairCover == null ? null : repairCover.trim();
    }

    public Integer getRepairPopularity() {
        return repairPopularity;
    }

    public void setRepairPopularity(Integer repairPopularity) {
        this.repairPopularity = repairPopularity;
    }

    public Double getRepairLongitude() {
        return repairLongitude;
    }

    public void setRepairLongitude(Double repairLongitude) {
        this.repairLongitude = repairLongitude;
    }

    public Double getRepairLatitude() {
        return repairLatitude;
    }

    public void setRepairLatitude(Double repairLatitude) {
        this.repairLatitude = repairLatitude;
    }

    public String getRepairAddress() {
        return repairAddress;
    }

    public void setRepairAddress(String repairAddress) {
        this.repairAddress = repairAddress == null ? null : repairAddress.trim();
    }

    public String getRepairPhone() {
        return repairPhone;
    }

    public void setRepairPhone(String repairPhone) {
        this.repairPhone = repairPhone == null ? null : repairPhone.trim();
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