package com.example.capstone_project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    Button menu_btn[];
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_select);

        myHelper = new MyDBHelper(this);

        menu_btn = new Button[6];

        Integer btn_id[] = {R.id.Relative_btn, R.id.Mercenary_btn, R.id.Stadium_btn,
                R.id.Promotion_btn, R.id.Declaration_btn, R.id.Preferences_btn};

        for (int i = 0; i < menu_btn.length; i++) {
            menu_btn[i] = findViewById(btn_id[i]);
        }

        menu_btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RelativeBoardActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MercenaryBoardActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, StadiumSelectActivity.class);
                startActivity(intent);

            }
        });

        menu_btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TeamBoardActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, StadiumSelectActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();


                /*
                //MyDBHelper의 onUpgrade로 가서 drop 후 oncreate로 다시 테이블 생성( 실질적인 초기화 역할 )
                myHelper.onUpgrade(sqlDB, 0, 1);
                Toast.makeText(getApplicationContext(), "모두 삭제됨", Toast.LENGTH_SHORT).show();

                sqlDB.execSQL("INSERT INTO stadiumTBL (name, address, time, charge, tel, image)\n" +
                        "VALUES\n" +
                        "('월드컵 경기장','서울특별시 마포구 성산2동 월드컵로 240','06:00 ~ 24:00','유료','02-2128-2002','https://dimg.donga.com/wps/NEWS/IMAGE/2017/08/29/86070311.3.jpg'),\n" +
                        "('난지천 공원 축구장','서울특별시 마포구 상암동','18:00 ~ 22:00','유료','02-3153-9874','https://t1.daumcdn.net/cfile/tistory/1308423C4E621B2833'),\n" +
                        "('한강시민공원 축구장','서울특별시 영등포구 여의도동','06:00 ~ 20:00','유료','02-782-2898','https://www.mangosports.co.kr/file_contents/space_images/1419__img3.png'),\n" +
                        "('의왕 축구장','경기도 의왕시 포일동 155-2','06:00 ~ 24:00','유료','031-426-1300','https://fcant.club/packs/media/images/soccer-field1-88b425be616644a6fac64b101818299b.jpg'),\n" +
                        "('자유공원 축구장','경기도 안양시 동안구 호계동 1112','08:00 ~ 24:00','유료','031-8045-2413','https://www.ansi.or.kr/ansi01/common/img/business/img_busi0801.gif'),\n" +
                        "(' ',' ',' ',' ',' ',' ')");

                sqlDB.execSQL("INSERT INTO placeTBL (name, address, bddress)\n" +
                        "VALUES\n" +
                        "('월드컵 경기장','37.568256', '126.897240'),\n" +
                        "('난지천 공원 축구장','37.5741465', '126.8816503'),\n" +
                        "('한강시민공원 축구장','37.5342321', '126.9150377'),\n" +
                        "('의왕 축구장','37.3882791', '126.9861281'),\n" +
                        "('자유공원 축구장','37.3808392', '126.9613703'),\n" +
                        "(' ',' ',' ')");
                Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
                sqlDB.close();


                 */

                Intent intent = new Intent(MenuActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });
    }
}