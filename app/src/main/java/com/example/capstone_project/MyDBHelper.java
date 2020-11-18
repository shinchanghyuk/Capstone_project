package com.example.capstone_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {

        super(context, "stadiumDB", null, 1);
    }

    @Override
    //테이블 생성
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE stadiumTBL (Name CHAR(40) PRIMARY KEY, Address CHAR(40), time CHAR(20), Charge CHAR(20), tel CHAR(20), image CHAR(80));");
        sqLiteDatabase.execSQL("CREATE TABLE placeTBL (Name CHAR(40) PRIMARY KEY, Address CHAR(20), Bddress CHAR(20));");
    }

    @Override
    //초기화 버튼에 의해 실행되는 초기화 루틴
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //테이블 삭제
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS stadiumTBL");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS placeTBL");
        //후 생성
        onCreate(sqLiteDatabase);
    }
}