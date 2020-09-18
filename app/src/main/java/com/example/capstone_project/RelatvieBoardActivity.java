package com.example.capstone_project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RelatvieBoardActivity extends AppCompatActivity {
    Button myboard_btn, write_btn, alarm_btn;
    Spinner cat;
    ArrayList<String> tm_cat;
    ArrayAdapter<String> tm_catAdapter;

    RelativeBoardAdapter tmAdapter;
    RecyclerView tm_RecyclerView;
    ArrayList<RelativeBoardItem> tm_list = new ArrayList<RelativeBoardItem>();
    String username;
    String userName[], title[], matchDate[], uploadDate[], matchCheck[], num[], region[], date[], cont[], ability[];
    String  newUpdate = null;
    final static int CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_board);

        myboard_btn = findViewById(R.id.myboard_btn);
        write_btn = findViewById(R.id.write_btn);
        alarm_btn = findViewById(R.id.alarm_btn);

        Intent intent = getIntent();
        username = intent.getExtras().getString("name");

        tm_cat = new ArrayList<>();
        tm_cat.add("지역");
        tm_cat.add("매칭현황");
        tm_cat.add("날짜");
        tm_cat.add("실력");
        tm_cat.add("작성자");

        cat = findViewById(R.id.search_spinner);

        tm_catAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tm_cat);
        cat.setAdapter(tm_catAdapter);

        tm_RecyclerView = findViewById(R.id.tmRecyclerV);
        tm_RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userName = new String[20];
        title = new String[20];
        matchDate = new String[20];
        uploadDate = new String[20];
        matchCheck = new String[20];
        num = new String[20];
        region = new String[20];
        date = new String[20];
        cont = new String[20];
        ability = new String[20];

        tmAdapter = new RelativeBoardAdapter(tm_list, userName, title, matchDate, num, region, date, cont, ability);
        tm_RecyclerView.setAdapter(tmAdapter);

        tm_RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        num[0] = "9"; num[1] = "9"; num[2] = "11"; num[3] = "11"; num[4] = "9"; num[5] = "9";

        userName[0] = "김준호"; userName[1] = "신창혁"; userName[2] = "서이삭";
        userName[3] = "김영진"; userName[4] = "신창혁"; userName[5] = "김한솔";

        title[0] = "광진구 매칭구해요."; title[1] = "은평구 매칭구해요."; title[2] = "서대문구 매칭구함.";
        title[3] = "은평구 매칭구해요."; title[4] = "9대9 매칭구해요."; title[5] = "은평구 매칭구함.";

        matchDate[0] = "6/28"; matchDate[1] = "6/27"; matchDate[2] = "6/27";
        matchDate[3] = "6/25"; matchDate[4] = "6/24"; matchDate[5] = "6/24";

        uploadDate[0] = "06/23"; uploadDate[1] = "06/23"; uploadDate[2] = "06/22";
        uploadDate[3] = "06/21"; uploadDate[4] = "06/21"; uploadDate[5] = " 06/20";

        matchCheck[0] = " 매칭중"; matchCheck[1] = " 매칭중"; matchCheck[2] = " 매칭중";
        matchCheck[3] = " 매칭중"; matchCheck[4] = "매칭완료"; matchCheck[5] = "매칭완료";

        region[0] = "서울특별시 광진구"; region[1] = "서울특별시 은평구"; region[2] = "서울특별시 서대문구";
        region[3] = "서울특별시 은평구"; region[4] = "서울특별시 은평구"; region[5] = "서울특별시 은평구";

        date[0] = "20/06/28"; date[1] = "20/06/27"; date[2] = "20/06/27";
        date[3] = "20/06/25"; date[4] = "20/06/24"; date[5] = "20/06/24";

        cont[0] = "광진구에서 시합하실 상대팀 구합니다\n\n 매너 겜 하실 분 연락주세요!!";
        cont[1] = "증산 체육공원에서 경기 하실 상대팀 찾습니다.\n\n저녁 6시부터 저녁 8시까지 할 팀 구해요!!";
        cont[2] = "경기 하실 팀 구합니다. \n\n연락주세요!!";
        cont[3] = "매너 겜 하실 상대팀 구합니다.\n\n축구 하고 싶어요";
        cont[4] = "증산 체육공원에서 9대9 시합하실 상대팀 구합니다.\n\n연락주세요!!";
        cont[5] = "즐겁게 공 차실 상대팀 구해요~~";

        ability[0] = "하"; ability[1] = "하"; ability[2] = "하";
        ability[3] = "하"; ability[4] = "하"; ability[5] = "하";

        tmAdapter.notifyDataSetChanged();
        // 리사이클 뷰 갱신

        for (int i = 5; i >= 0; i--) {
            tmb_addItem(matchCheck[i], matchDate[i] + " " + title[i], uploadDate[i], userName[i]);
        } // 리사이클 뷰 목록 추가

        // 내 게시판 버튼 눌렀을 때 동작
        myboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelatvieBoardActivity.this, RelativeMyBoardActivity.class);
                int i = 0;
                for (i = 0; i < userName.length; i++) {
                    if (userName[i] == username) {
                        intent.putExtra("region", region[i]);
                        intent.putExtra("date", matchDate[i]);
                        intent.putExtra("title", title[i]);
                        intent.putExtra("ability", ability[i]);
                        intent.putExtra("num", num[i]);
                        intent.putExtra("con", cont[i]);
                        intent.putExtra("check", matchCheck[i]);
                        intent.putExtra("newBoard", true);
                    }
                }
                intent.putExtra("name", username);
                startActivity(intent);
            }
        });
        // 글쓰기 버튼을 눌렀을 때 동작
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelatvieBoardActivity.this, RelativeWritingActivity.class);
                startActivityForResult(intent, CODE);
            }
        });
        // 알람 버튼을 눌렀을 때 동작
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelatvieBoardActivity.this, RelativeAlarmActivity.class);
                startActivity(intent);
            }
        });
    }

    public void tmb_addItem(String check, String con, String w_day, String w_user){
        RelativeBoardItem item = new RelativeBoardItem();

        item.setMatching_check(check);
        item.setContents(con);
        item.setWrite_day(w_day);
        item.setWrite_user(w_user);

        tm_list.add(0, item);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i = title.length - 2;
        if(RESULT_OK == resultCode){
            int up = 1;
            up += i;

            ability[up] = ability[i];
            userName[up] = userName[i];
            title[up] = title[i];
            matchDate[up] = matchDate[i];
            matchCheck[up] = matchCheck[i];
            num[up] = num[i];
            region[up] = region[i];
            cont[up] = cont[i];
            ability[up] = ability[i];

            for (int j = 19; j >= 1; j--){
                userName[j] = userName[j-1];
                title[j] = title[j-1];
                matchDate[j] = matchDate[j-1];
                matchCheck[j] = matchCheck[j-1];
                cont[j] = cont[j-1];
                num[j] = num[j-1];
                region[j] = region[j-1];
                ability[j] = ability[j-1];
            }

            title[0] =data.getStringExtra("title");
            region[0]= data.getStringExtra("region");
            matchDate[0] = data.getStringExtra("date");
            matchCheck[0] = "매칭중";
            ability[0] = data.getStringExtra("ability");
            userName[0] = username;
            cont[0] = data.getStringExtra("con");
            num[0] = data.getStringExtra("num");

            Date currentTime = Calendar.getInstance().getTime();
            newUpdate = new SimpleDateFormat("MM/dd", Locale.getDefault()).format(currentTime);

            tmb_addItem(" "+ matchCheck[0], matchDate[0]+" "+title[0] ,  newUpdate, username);
            tmAdapter.notifyDataSetChanged();
        }
    }
}