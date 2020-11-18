package com.example.capstone_project.mypage;

public class Manager {
    private String title, writetime, name, uid, boardnumber, content;

    public Manager() {
    }

    public Manager(String name, String uid, String boardnumber, String title, String writetime, String content) {
        this.name = name;
        this.uid = uid;
        this.boardnumber = boardnumber;
        this.title = title;
        this.writetime = writetime;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWritetime() {
        return writetime;
    }

    public void setWritetime(String writetime) {
        this.writetime = writetime;
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

    public void setBoardnumber(String boardnumber) {
        this.boardnumber = boardnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}