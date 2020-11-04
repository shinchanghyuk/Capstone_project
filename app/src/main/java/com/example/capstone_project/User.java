package com.example.capstone_project;

public class User {
    private String name;
    private String uid;
    private String loginWay;
    private String userToken;

    public User() {
    }

    public User(String name, String uid, String loginWay, String userToken) {
        this.name = name;
        this.uid = uid;
        this.loginWay = loginWay;
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
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
