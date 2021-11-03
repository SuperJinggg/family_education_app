package com.example.ateachingapplication.domain;

import java.io.Serializable;

public class BookOrder implements Serializable {
    private Integer id;

    private String parPhone;

    private String bookName;

    private Integer counts;

    private Double price;

    private Integer states;

    public BookOrder(Integer id, String parPhone, String bookName, Integer counts, Double price, Integer states) {
        this.id = id;
        this.parPhone = parPhone;
        this.bookName = bookName;
        this.counts = counts;
        this.price = price;
        this.states = states;
    }

    public BookOrder() {
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

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer count) {
        this.counts = count;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStates() {
        return states;
    }

    public void setStates(Integer state) {
        this.states = state;
    }
}