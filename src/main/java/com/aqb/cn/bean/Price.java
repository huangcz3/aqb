package com.aqb.cn.bean;

import java.util.Date;

public class Price {
    private String id;

    private Date priceStart;

    private String priceStartString;

    private Date priceEnd;

    private String priceEndString;

    private Float priceJiage;

    private String priceCity;

    private Long times;
    
    public Long getTimes() {
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}

	private Integer status;

    private Date foundDate;

    public String getPriceStartString() {
        return priceStartString;
    }

    public void setPriceStartString(String priceStartString) {
        this.priceStartString = priceStartString;
    }

    public String getPriceEndString() {
        return priceEndString;
    }

    public void setPriceEndString(String priceEndString) {
        this.priceEndString = priceEndString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(Date priceStart) {
        this.priceStart = priceStart;
    }

    public Date getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(Date priceEnd) {
        this.priceEnd = priceEnd;
    }

    public Float getPriceJiage() {
        return priceJiage;
    }

    public void setPriceJiage(Float priceJiage) {
        this.priceJiage = priceJiage;
    }

    public String getPriceCity() {
        return priceCity;
    }

    public void setPriceCity(String priceCity) {
        this.priceCity = priceCity == null ? null : priceCity.trim();
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