package com.example.capstone_project;

import java.util.ArrayList;

public class Board {
    private String matching;
    private String title;
    private String day;
    private String user;
    private String place;
    private String starttime;
    private String endtime;
    private String ability;
    private String person;
    private String boardnumber;
    private String content;
    // private String uid;

    public Board(String matching, String title, String day, String user, String place, String starttime, String boardnumber, String endtime, String ability, String person, String content) {
        this.matching = matching;
        this.title = title;
        this.day = day;
        this.user = user;
        this.place = place;
        this.starttime = starttime;
        this.boardnumber = boardnumber;
        this.endtime = endtime;
        this.ability = ability;
        this.person = person;
        this.content = content;
    }

    public String getMatching() {
        return matching;
    }

    public void setMatching(String matching) {
        this.matching = matching;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBoardnumber() {
        return boardnumber;
    }

    public void setBoardnumber(String number) {
        this.boardnumber = number;
    }
}

