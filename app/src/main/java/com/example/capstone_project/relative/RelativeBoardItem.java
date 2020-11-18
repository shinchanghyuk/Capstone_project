package com.example.capstone_project.relative;

public class RelativeBoardItem {
    private String matching, title, day, user, ability, content, endtime,
            person, starttime, place, boardnumber, uid, writetime;

    public RelativeBoardItem(String matching, String title, String day, String user, String place, String starttime,
                             String boardnumber, String endtime, String ability, String person, String content, String uid, String writetime) {
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
        this.uid = uid;
        this.writetime = writetime;
    }

    public String getWritetime() {
        return writetime;
    }

    public void setWritetime(String writetime) {
        this.writetime = writetime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBoardnumber() {
        return boardnumber;
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

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setBoardnumber(String boardnumber) {
        this.boardnumber = boardnumber;
    }

    public RelativeBoardItem() {
    }

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