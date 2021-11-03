package com.example.ateachingapplication.domain;

import java.io.Serializable;

public class Teacher implements Serializable {
    private Integer id;

    private String teacherName;

    private String teacherSex;

    private String teacherPhone;

    private String teacherAddress;

    private String teacherIcon;

    private String teacherExper;

    private String subject;

    private String grade;

    public Teacher(Integer id, String teacherName, String teacherSex, String teacherPhone, String teacherAddress, String teacherIcon, String teacherExper, String subject, String grade) {
        this.id = id;
        this.teacherName = teacherName;
        this.teacherSex = teacherSex;
        this.teacherPhone = teacherPhone;
        this.teacherAddress = teacherAddress;
        this.teacherIcon = teacherIcon;
        this.teacherExper = teacherExper;
        this.subject = subject;
        this.grade = grade;
    }

    public Teacher() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherSex() {
        return teacherSex;
    }

    public void setTeacherSex(String teacherSex) {
        this.teacherSex = teacherSex;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getTeacherAddress() {
        return teacherAddress;
    }

    public void setTeacherAddress(String teacherAddress) {
        this.teacherAddress = teacherAddress;
    }

    public String getTeacherIcon() {
        return teacherIcon;
    }

    public void setTeacherIcon(String teacherIcon) {
        this.teacherIcon = teacherIcon;
    }

    public String getTeacherExper() {
        return teacherExper;
    }

    public void setTeacherExper(String teacherExper) {
        this.teacherExper = teacherExper;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}