package com.example.ateachingapplication.domain;

import java.io.Serializable;
import java.sql.Date;

public class ParReward implements Serializable {
    private Integer id;

    private String parPhone;

    private Double cost;

    private Date endDate;

    public ParReward() {
        super();
    }

    public ParReward(Integer id, String parPhone, Double cost, Date endDate) {
        this.id = id;
        this.parPhone = parPhone;
        this.cost = cost;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParPhone() {
        return parPhone;
    }

    public void setParPhone(String parPhone) {
        this.parPhone = parPhone;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}