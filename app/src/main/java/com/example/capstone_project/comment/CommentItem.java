package com.example.capstone_project.comment;

public class CommentItem {
    private String user, writetime, content, recomcount, boardnumber, commentnum, uid;

    public CommentItem(String boardnumber, String commentnum, String user, String writetime, String content, String recomcount, String uid) {

        this.boardnumber = boardnumber;
        this.commentnum = commentnum;
        this.user = user;
        this.writetime = writetime;
        this.recomcount = recomcount;
        this.content = content;
        this.uid = uid;
    }

    public CommentItem() {
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

    public void setBoardnumber(String boardNumber) {
        this.boardnumber = boardNumber;
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
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

    public String getRecomcount() {
        return recomcount;
    }

    public void setRecomcount(String recomcount) {
        this.recomcount = recomcount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}