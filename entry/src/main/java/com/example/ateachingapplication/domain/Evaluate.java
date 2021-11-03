package com.example.ateachingapplication.domain;

import java.io.Serializable;
import java.sql.Date;

public class Evaluate implements Serializable {
    private Integer id;

    private String parPhone;

    private String teacherName;

    private String teacherPhone;

    private String content;

    private Date dates;

    private String subject;

    public Evaluate(Integer id, String parPhone, String teacherName, String teacherPhone, String content, Date date, String subject) {
        this.id = id;
        this.parPhone = parPhone;
        this.teacherName = teacherName;
        this.teacherPhone = teacherPhone;
        this.content = content;
        this.dates = date;
        this.subject = subject;
    }

    public Evaluate() {
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

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDates() {
        return dates;
    }

    public void setDates(Date date) {
        this.dates = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}