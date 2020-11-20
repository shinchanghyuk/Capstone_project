package com.example.capstone_project.stadium;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.capstone_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StadiumDetailsActivity extends AppCompatActivity {
    private TextView stadium_address, stadium_time, stadium_charge,
            stadium_telephone, stadium_name, stadium_page; // 경기장의 정보들을 보여주는 텍스트 뷰 선언
    private RecyclerView recyclerView; // 리사이클러뷰 선언
    private RecyclerView.LayoutManager layoutManager; // 레이아웃 매니저 선언
    private RecyclerView.Adapter adapter; // 리사이클러뷰 어댑터 선언
    private ArrayList<StadiumItem> arrayList; // 아이템 담을 배열리스트 선언
    private String stadiumAddress, stadiumTime, stadiumCharge, stadiumTelephone, stadiumName, stadiumPage; // 사용자가 선택한 경기장 이름을 담을 변수 선언
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference stadium_database; // 파이버에시스 연결(경로) 선언
    private List<String> stadiumImageList; // 경기장의 이미지 url을 담는 배열리스트

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_details);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

    }
    public void init() {
        stadium_address = (TextView) findViewById(R.id.stadium_address);
        stadium_time = (TextView) findViewById(R.id.stadium_time);
        stadium_charge = (TextView) findViewById(R.id.stadium_charge);
        stadium_telephone = (TextView) findViewById(R.id.stadium_tele);
        stadium_page = findViewById(R.id.stadium_page);
        stadium_name = findViewById(R.id.stadium_textView);
        recyclerView = findViewById(R.id.stadium_recyclerview);

        recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함
        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        Intent intent = getIntent();
        stadiumName = intent.getExtras().getString("stadium_name");
        // 리사이클러뷰 목록에서 사용자가 선택한 경기장 이름을 가져와 변수에 넣음

        stadium_name.setText(stadiumName);
        // 경기장 이름 텍스트뷰에 사용자가 선택한 경기장 이름을 입힘

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        stadium_database = firebaseDatabase.getReference("stadium");
        // stadium 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = stadium_database.orderByChild("name").equalTo(stadiumName);
        // stadium 키에 있는 name과 받아온 stadiumname를 비교

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                    StadiumItem stadiumItem = snapshot.getValue(StadiumItem.class);
                    // StadiumItem 객체에 데이터를 담음

                    stadiumAddress = stadiumItem.getAddress();
                    stadiumTime = stadiumItem.getTime();
                    stadiumCharge = stadiumItem.getCharge();
                    stadiumTelephone = stadiumItem.getTelephone();
                    stadiumImageList = stadiumItem.getImage();
                    stadiumPage = stadiumItem.getPage();

                    Log.d("sta", String.valueOf(stadiumImageList));
                    // 데이터베이스에서 일치하는 게시물의 정보들을 가져옴
                }
                stadium_address.setText(stadiumAddress);
                stadium_time.setText(stadiumTime);
                stadium_charge.setText(stadiumCharge);
                stadium_telephone.setText(stadiumTelephone);
                stadium_page.setText(stadiumPage);
                // 가져온 정보들을 텍스트 뷰에 보여줌

                adapter = new StadiumInfoAdapter(stadiumImageList);
                // StadiumInfoAdapter 객체들을 생성
                recyclerView.setAdapter(adapter);
                // 리사이클러뷰에 상대매칭 게시판에 등록 된 데이터들을 담음
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
    }
}