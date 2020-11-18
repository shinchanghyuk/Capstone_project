package com.example.capstone_project.report;

public class ReportItem {
    private String boardtype, user, writetime, check, content, boardnumber, reportnumber, uid, type;

    public ReportItem() {
    }

    public ReportItem(String boardnumber, String reportnumber, String user, String uid, String writetime,
                      String check, String content, String type, String boardtype) {

        this.boardnumber = boardnumber;
        this.reportnumber = reportnumber;
        this.user = user;
        this.uid = uid;
        this.writetime = writetime;
        this.content = content;
        this.check = check;
        this.type = type;
        this.boardtype = boardtype;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBoardtype() {
        return boardtype;
    }

    public void setBoardtype(String boardtype) {
        this.boardtype = boardtype;
    }

    public String getWritetime() {
        return writetime;
    }

    public void setWritetime(String writetime) {
        this.writetime = writetime;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
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

    public String getReportnumber() {
        return reportnumber;
    }

    public void setReportnumber(String reportnumber) {
        this.reportnumber = reportnumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
