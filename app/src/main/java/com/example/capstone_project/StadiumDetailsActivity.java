package com.example.capstone_project;

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

import java.io.ByteArrayOutputStream;

public class StadiumDetailsActivity extends AppCompatActivity {

    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;

    String na,ad,ti,ch,te;
    String stadiumName;

    int i =0;

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

            //na = na.replace("\r","").replace("\n","");
            //Toast.makeText(getApplicationContext(), stadiumName+1+1+na+1+1, Toast.LENGTH_SHORT).show();

            if(stadiumName.equals(na)){
                stadium_nameTv.setText(na);
                stadium_address.setText(ad);
                stadium_time.setText(ti);
                stadium_charge.setText(ch);
                stadium_tele.setText(te);
                stadium_image.setImageResource(R.drawable.a);
           }


            i++;
            cursor.close();
            sqlDB.close();
        }

/*
        if(stadium.equals("m0")){
            //월드컵
            stadium_address.setText("서울특별시 마포구 성산2동 월드컵로 240");
            stadium_time.setText("06:00 ~ 24:00");
            stadium_charge.setText("유료");
            stadium_tele.setText("02-2128-2002");
            stadium_image.setImageResource(R.drawable.a);
        } else if(stadium.equals("m1")){
            //난지천
            stadium_address.setText("서울특별시 마포구 상암동");
            stadium_time.setText("18:00 ~ 22:00");
            stadium_charge.setText("유료");
            stadium_tele.setText("02-3153-9874");
            stadium_image.setImageResource(R.drawable.b);
        }else if(stadium.equals("m2")){
            //한강시민공원
            stadium_address.setText("서울특별시 영등포구 여의도동");
            stadium_time.setText("06:00 ~ 20:00");
            stadium_charge.setText("유료");
            stadium_tele.setText("02-782-2898");
            stadium_image.setImageResource(R.drawable.c);
        }else if(stadium.equals("m3")){
            //의왕
            stadium_address.setText("경기도 의왕시 포일동 155-2");
            stadium_time.setText("06:00 ~ 24:00");
            stadium_charge.setText("유료");
            stadium_tele.setText("031-426-1300");
            stadium_image.setImageResource(R.drawable.d);
        }else if(stadium.equals("m4")){
            //자유공원
            stadium_address.setText("경기도 안양시 동안구 호계동 1112");
            stadium_time.setText("08:00 ~ 24:00");
            stadium_charge.setText("유료");
            stadium_tele.setText("031-8045-2413");
            stadium_image.setImageResource(R.drawable.e);
        }


 */
    }
}
