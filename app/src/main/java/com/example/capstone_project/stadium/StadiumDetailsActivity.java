package com.example.capstone_project.stadium;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.capstone_project.MyDBHelper;
import com.example.capstone_project.R;

import java.io.ByteArrayOutputStream;

public class StadiumDetailsActivity extends AppCompatActivity {

    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    String na,ad,ti,ch,te,im;
    String stadiumName;
    int i =0; //카운트 변수

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_details);

        TextView stadium_address = (TextView) findViewById(R.id.stadium_address);
        TextView stadium_time = (TextView) findViewById(R.id.stadium_time);
        TextView stadium_charge = (TextView) findViewById(R.id.stadium_charge);
        TextView stadium_tele = (TextView) findViewById(R.id.stadium_tele);

        TextView stadium_nameTv = findViewById(R.id.stadium_nameTv);
        ImageView stadium_image = (ImageView) findViewById(R.id.stadium_imageView);

        Intent intent = getIntent();
        stadiumName = intent.getExtras().getString("stadiumname");

        stadium_nameTv.setText(stadiumName);

        //생성자(MyDBHelper 생성)
        myHelper = new MyDBHelper(this);

        //db 가져오기
        sqlDB = myHelper.getReadableDatabase();
        //포인터 역할
        Cursor cursor;
        //select문을 사용하기 위한 rawQuery
        cursor = sqlDB.rawQuery("SELECT * FROM stadiumTBL;", null);

        //자료 0개 일 경우
        if (cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "DB값 없음.", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()){
            na = cursor.getString(0);
            ad = cursor.getString(1);
            ti = cursor.getString(2);
            ch = cursor.getString(3);
            te = cursor.getString(4);
            im = cursor.getString(5);

            if(stadiumName.equals(na)){
                stadium_nameTv.setText(na);
                stadium_address.setText(ad);
                stadium_time.setText(ti);
                stadium_charge.setText(ch);
                stadium_tele.setText(te);
                Glide.with(this).load(im).into(stadium_image);
            }
            i++;
        }
        cursor.close();
        sqlDB.close();
    }
}