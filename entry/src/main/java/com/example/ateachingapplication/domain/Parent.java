package com.example.ateachingapplication.domain;


import java.io.Serializable;

public class Parent implements Serializable {
    private Integer id;

    private String parPhone;

    private String password;

    private String icon;

    private String address;

    public Parent(Integer id, String parPhone, String password, String icon, String address) {
        this.id = id;
        this.parPhone = parPhone;
        this.password = password;
        this.icon = icon;
        this.address = address;
    }

    public Parent() {
        super();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}