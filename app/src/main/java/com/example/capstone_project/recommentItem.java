package com.example.capstone_project;

public class RecommentItem {
    private String user, writetime, content, commentnum, recommentnum, uid, boardnumber;

    public RecommentItem(String commentnum, String boardnumber , String recommentnum, String user, String writetime, String content, String uid) {

        this.commentnum = commentnum;
        this.boardnumber = boardnumber;
        this.recommentnum = recommentnum;
        this.user = user;
        this.writetime = writetime;
        this.content = content;
        this.uid = uid;

    }

    public RecommentItem() {
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
    }

    public String getBoardnumber() {
        return boardnumber;
    }

    public void setBoardnumber(String boardnumber) {
        this.boardnumber = boardnumber;
    }

    public String getRecommentnum() {
        return recommentnum;
    }

    public void setRecommentnum(String recommentnum) {
        this.recommentnum = recommentnum;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}