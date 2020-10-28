package com.example.capstone_project;

public class User {
    private String name;
    private String uid;
    private String loginWay;

    public User() {
    }

    public User(String name, String uid, String loginWay) {
        this.name = name;
        this.uid = uid;
        this.loginWay = loginWay;
    }
    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginWay() {
        return loginWay;
    }

    public void setLoginWay(String loginWay) {
        this.loginWay = loginWay;
    }
}
