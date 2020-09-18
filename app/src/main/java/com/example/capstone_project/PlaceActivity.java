package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceActivity extends AppCompatActivity {

    Button endPlaceBtn;
    ListView rightList;
    BoardAdapter boardAdapter;
    RecyclerView main_listview;
    ArrayList<BoardItem> list = new ArrayList<BoardItem>();
    String pltxt = " ", becla;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);

        Intent intent = getIntent();
        becla = intent.getExtras().getString("class");

        rightList = findViewById(R.id.rightList);
        endPlaceBtn = findViewById(R.id.endPlaceBtn);
        main_listview = findViewById(R.id.main_listview);

        main_listview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        boardAdapter = new BoardAdapter(list);
        main_listview.setAdapter(boardAdapter);

        main_listview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String txt;
                txt = parent.getItemAtPosition(position).toString();
                re_addItem(txt);
                boardAdapter.notifyItemChanged(position);
                pltxt += " " + txt;
            }
        });

        endPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(becla){
                    case "A" : ((RelativeWritingActivity) RelativeWritingActivity.mContext).incomA(pltxt);
                    finish();
                    break;
                    case "B":;
                    case "c":;
                    case "D" :((RelativeAlarmActivity) RelativeAlarmActivity.maContext).incomD(pltxt);
                    finish();
                    break;
                }
            }
        });
    }
    public void onBackPressed() {

        switch(becla){
            case "A" : ((RelativeWritingActivity) RelativeWritingActivity.mContext).incomA(pltxt);
                finish();
                break;
            case "B":;
            case "c":;
            case "D" :((RelativeAlarmActivity) RelativeAlarmActivity.maContext).incomD(pltxt);
            finish();
            break;
        }
        super.onBackPressed();
    }
    public void re_addItem(String place){
        BoardItem item = new BoardItem();

        item.setPlace(place);
        list.add(item);
    }
}


