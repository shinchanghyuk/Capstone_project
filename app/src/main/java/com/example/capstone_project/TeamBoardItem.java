package com.example.capstone_project;

import android.content.Context;

import java.util.ArrayList;

public class TeamBoardItem {
    private String matching, title, day, user, ability, content, name;
    private String person, place, boardnumber;
    public TeamBoardItem() {

    }
    public TeamBoardItem(String matching, String title, String day, String user, String place, String name, String boardnumber, String ability, String person, String content) {
        this.matching = matching;
        this.title = title;
        this.day = day;
        this.user = user;
        this.place = place;
        this.name = name;
        this.boardnumber = boardnumber;
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

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBoardnumber() {
        return boardnumber;
    }

    public void setBoardnumber(String boardnumber) {
        this.boardnumber = boardnumber;
    }
}

