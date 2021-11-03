package com.example.ateachingapplication.domain;

import java.io.Serializable;
import java.sql.Date;

public class Plan implements Serializable {
    private Integer id;
    //计划人
    private String parPhone;
    //计划内容
    private String planContent;
    //计划开始时间
    private Date startDate;
    //计划结束时间
    private Date endDate;

    public Plan() {
    }

    public Plan(Integer id, String parPhone, String planContent, Date startDate, Date endDate) {
        this.id = id;
        this.parPhone = parPhone;
        this.planContent = planContent;
        this.startDate = startDate;
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

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", parPhone='" + parPhone + '\'' +
                ", planContent='" + planContent + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}