package com.example.capstone_project;

public class User {
    private String name, uid, loginWay, userToken, replace, redate, meplace, medate, realarm, mealarm, noticealarm;
    public User() {
    }

    public User(String name, String uid, String loginWay, String userToken, String realarm, String mealarm, String replace, String redate, String meplace, String medate, String noticealarm) {
        this.name = name;
        this.uid = uid;
        this.loginWay = loginWay;
        this.userToken = userToken;
        this.replace = replace;
        this.redate = redate;
        this.meplace = meplace;
        this.medate = medate;
        this.realarm = realarm;
        this.mealarm = mealarm;
        this.noticealarm = noticealarm;
    }

    public User(String name, String uid, String loginWay, String userToken, String realarm, String mealarm, String noticealarm) {
        this.name = name;
        this.uid = uid;
        this.loginWay = loginWay;
        this.userToken = userToken;
        this.realarm = realarm;
        this.mealarm = mealarm;
        this.noticealarm = noticealarm;
    }
    public String getNoticealarm() {
        return noticealarm;
    }

    public void setNoticealarm(String noticealarm) {
        this.noticealarm = noticealarm;
    }


    public String getRealarm() {
        return realarm;
    }

    public void setRealarm(String realarm) {
        this.realarm = realarm;
    }

    public String getMealarm() {
        return mealarm;
    }

    public void setMealarm(String mealarm) {
        this.mealarm = mealarm;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public String getRedate() {
        return redate;
    }

    public void setRedate(String redate) {
        this.redate = redate;
    }

    public String getMeplace() {
        return meplace;
    }

    public void setMeplace(String meplace) {
        this.meplace = meplace;
    }

    public String getMedate() {
        return medate;
    }

    public void setMedate(String medate) {
        this.medate = medate;
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
