package com.example.ateachingapplication.domain;

import java.io.Serializable;
import java.sql.Date;

public class CAward implements Serializable {
    private Double cost;

    private Date endDate;

    private int price;

    public CAward(Double cost, Date endDate, int price) {
        this.cost = cost;
        this.endDate = endDate;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
