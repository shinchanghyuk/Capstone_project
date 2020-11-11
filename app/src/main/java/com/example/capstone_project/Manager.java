package com.example.capstone_project;

public class Manager {
    private String name, uid, boardnumber;

    public Manager() {
    }

    public Manager(String name, String uid, String boardnumber) {
        this.name = name;
        this.uid = uid;
        this.boardnumber = boardnumber;
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