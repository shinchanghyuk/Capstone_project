package com.example.capstone_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        //db생성, 첫 작업이기에 old version이 null
        super(context, "stadiumDB", null, 1);
    }

    @Override
    //테이블 생성
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE stadiumTBL (Name CHAR(40) PRIMARY KEY, Address CHAR(40), time CHAR(20), Charge CHAR(5), tel CHAR(20));");
    }

    @Override
    //초기화 버튼에 의해 실행되는 초기화 루틴
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //테이블 삭제
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS stadiumTBL");
        //후 생성
        onCreate(sqLiteDatabase);
    }
}