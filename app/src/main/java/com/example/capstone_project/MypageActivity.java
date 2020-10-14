package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MypageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;  // 리사이클러뷰 선언
    private RecyclerView.LayoutManager layoutManager;  // 레이아웃 매니저 선언
    private RecyclerView.Adapter adapter;   // 리사이클러뷰 어댑터 선언
    private ArrayList<MypageItem> arrayList; // 아이템 담을 배열리스트

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        recyclerView = findViewById(R.id.mypage_recyclerview);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        arrayList = new ArrayList<>();

        List<String> listPage = Arrays.asList(getResources().getStringArray(R.array.mypage));

        for (int i = 0; i < listPage.size(); i++) {
            MypageItem mypageItem = new MypageItem();
            mypageItem.setTitle(listPage.get(i));
            mypageItem.setMoveImage(R.drawable.ic_arrow_forward_black_24dp);
            arrayList.add(mypageItem);
        }

        adapter = new MypageAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }
}

