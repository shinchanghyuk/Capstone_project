package com.example.capstone_project;

public class RecommentItem {
    private String user, writetime, content, commentnum, recommentnum;

    public RecommentItem(String commentnum, String recommentnum, String user, String writetime, String content) {

        this.commentnum = commentnum;
        this.recommentnum = recommentnum;
        this.user = user;
        this.writetime = writetime;
        this.content = content;

    }

    public RecommentItem() { }

    public String getCommentnum() {return commentnum;}

    public void setCommentnum(String commentnum) {this.commentnum = commentnum; }

    public String getRecommentnum() {return recommentnum;}

    public void setRecommentnum(String recommentnum) { this.recommentnum = recommentnum; }

    public String getUser() {return user;}

    public void setUser(String user) {this.user = user;}

    public String getWritetime() { return writetime; }

    public void setWritetime(String writetime) { this.writetime = writetime; }

    public String getContent() { return content;}

    public void setContent(String content) { this.content = content; }


}