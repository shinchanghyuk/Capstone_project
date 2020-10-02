package com.example.capstone_project;

public class RelativeBoardItem {
    private String matching;
    private String title;
    private String day;
    private String user;

    public RelativeBoardItem() { }
    public void setMatching(String matching) {
        this.matching = matching;
    }
    public String getMatching() {
        return matching;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getDay() {
        return day;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
}