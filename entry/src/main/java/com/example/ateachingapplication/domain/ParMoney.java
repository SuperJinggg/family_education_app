package com.example.ateachingapplication.domain;

import java.io.Serializable;

public class ParMoney implements Serializable {
    private Integer id;

    private String parPhone;

    private Double balance;

    private Integer point;

    public ParMoney() {
    }

    public ParMoney(Integer id, String parPhone, Double balance, Integer point) {
        this.id = id;
        this.parPhone = parPhone;
        this.balance = balance;
        this.point = point;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}