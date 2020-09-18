package com.example.capstone_project;

public class RelativeBoardItem {
    private String matching_check  = null;
    private String contents = null;
    private String write_day = null;
    private String write_user = null;

    public void setMatching_check (String m_check){
        matching_check = m_check;
    }
    public void setContents (String con){
        contents = con;
    }
    public void setWrite_day (String w_day){ write_day = w_day; }
    public  void setWrite_user(String w_user) {
        write_user = w_user;
    }
    public String getMatching_check() {
        return this.matching_check;
    }
    public String getContents() {
        return this.contents;
    }
    public String getWrite_day() {
        return this.write_day;
    }
    public String getWrite_user() {
        return this.write_user;
    }
}
