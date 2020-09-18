package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RelativeMyBoardActivity extends AppCompatActivity {
    boolean newBoard;

    String content[], matchDate[], uploadDate[], matchCheck[], title[], num[], date[], region[], ability[], newUpdate;
    String username;
    RelativeMyBoardAdapter mtmAdapter;
    RecyclerView mtm_RecyclerView;
    ArrayList<RelativeMyBoardItem> mtm_list = new ArrayList<RelativeMyBoardItem>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_myboard);

        Intent intent = getIntent();

        username = intent.getStringExtra("name");
        newBoard = intent.getBooleanExtra("newBoard", false);

        title = new String[5]; matchDate = new String[20]; num = new String[5];
        region = new String[5]; date = new String[5]; ability = new String[5]; content = new String[5];
        matchDate = new String[5]; uploadDate = new String[5]; matchCheck = new String[5];

        mtm_RecyclerView = findViewById(R.id.tmRecyclerV) ;
        mtm_RecyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        mtmAdapter = new RelativeMyBoardAdapter(mtm_list, username, title, matchDate,num,region,date,content,ability,matchCheck) ;
        mtm_RecyclerView.setAdapter(mtmAdapter) ;

        ability[0] = "하"; ability[1] ="하";
        region[0] = "서울특별시 은평구"; region[1] = "서울특별시 은평구";
        content[0] ="증산 체육공원에서 경기 하실 상대팀 찾습니다.\n\n저녁 6시부터 저녁 8시까지 할 팀 구해요!!";
        content[1] ="매너 겜 하실 상대팀 구합니다.\n\n축구 하고 싶어요";
        num[0] = "9"; num[1] = "9";
        title[0] = "은평구 매칭구해요."; title[1] = "9대9 매칭구해요.";
        matchDate[0] = " 6/27"; matchDate[1] = "6/24";
        uploadDate[0] = "6/23"; uploadDate[1] = "6/21";
        matchCheck[0] = "매칭중"; matchCheck[1] = "매칭완료";

        mtmb_addItem(matchCheck[1], matchDate[1]+" "+title[1], uploadDate[1], username);
        mtmb_addItem(matchCheck[0], matchDate[0]+" "+title[0], uploadDate[0], username);

        mtmAdapter.notifyDataSetChanged();

        if(newBoard == true) {
            int i = title.length - 2;
            int up = 1;
            up += i;

            title[up] = title[i];
            matchDate[up] = matchDate[i];
            matchCheck[up] = matchCheck[i];
            num[up] = num[i];
            region[up] = region[i];
            content[up] = content[i];
            ability[up] = ability[i];

            for (int j = 4; j >= 1; j--) {
                title[j] = title[j - 1];
                matchDate[j] = matchDate[j - 1];
                matchCheck[j] = matchCheck[j - 1];
                content[j] = content[j - 1];
                num[j] = num[j - 1];
                region[j] = region[j - 1];
                ability[j] = ability[j - 1];
            }

            title[0] = intent.getStringExtra("title");
            region[0] = intent.getStringExtra("region");
            matchDate[0] = intent.getStringExtra("date");
            matchCheck[0] = intent.getStringExtra("check");
            ability[0] = intent.getStringExtra("ability");
            content[0] = intent.getStringExtra("con");
            num[0] = intent.getStringExtra("num");

            Date currentTime = Calendar.getInstance().getTime();
            newUpdate = new SimpleDateFormat("M/dd", Locale.getDefault()).format(currentTime);

            mtmb_addItem(matchCheck[0] + " ", matchDate[0] + " " + title[0], " " + newUpdate, username);

            mtmAdapter.notifyDataSetChanged();
        }
    }
    public void mtmb_addItem(String check, String con, String w_day, String w_user){
        RelativeMyBoardItem item = new RelativeMyBoardItem();

        item.setMatching_check(check);
        item.setContents(con);
        item.setWrite_day(w_day);
        item.setWrite_user(w_user);

        mtm_list.add(0, item);
    }
}
