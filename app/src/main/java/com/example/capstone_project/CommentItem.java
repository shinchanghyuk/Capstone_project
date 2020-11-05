package com.example.capstone_project;

public class CommentItem { //댓글 구동시 사용되는 아이템들
    private String user, writetime, content, recomcount, boardnumber, commentnum;

    public CommentItem(String boardnumber, String commentnum, String user, String writetime, String content, String recomcount) {

        this.boardnumber = boardnumber;
        this.commentnum = commentnum;
        this.user = user;
        this.writetime = writetime;
        this.recomcount = recomcount;
        this.content = content;
    }

    public CommentItem() {
    }

    public String getBoardnumber() {
        return boardnumber;
    }

    public void setBoardnumber(String boardnumber) {
        this.boardnumber = boardnumber;
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