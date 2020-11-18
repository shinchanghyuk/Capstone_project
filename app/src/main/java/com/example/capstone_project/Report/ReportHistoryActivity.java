package com.example.capstone_project.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;
import com.example.capstone_project.mercenary.MercenaryBoardActivity;
import com.example.capstone_project.mypage.MypageActivity;
import com.example.capstone_project.relative.RelativeBoardActivity;
import com.example.capstone_project.stadium.StadiumSelectActivity;
import com.example.capstone_project.team.TeamBoardActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportHistoryActivity extends AppCompatActivity {
    private RecyclerView report_recyclerView; // 리사이클러뷰 선언
    private RecyclerView.LayoutManager report_manager; // 레이아웃 매니저 선언
    private RecyclerView.Adapter report_adapter; // 리사이클러뷰 어댑터 선언
    private ArrayList<ReportItem> report_arrayList; // 아이템 담을 배열리스트 선언
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference report_database; // 파이버에시스 연결(경로) 선언
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_history);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 바텀 네베게이션 뷰를 클릭 했을 때 동작
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_one:
                        Intent intent1 = new Intent(ReportHistoryActivity.this, RelativeBoardActivity.class);
                        startActivity(intent1);
                        // 상대매칭 아이템을 선택했을 때
                        break;
                    case R.id.action_two:
                        Intent intent2 = new Intent(ReportHistoryActivity.this, MercenaryBoardActivity.class);
                        startActivity(intent2);
                        // 용병모집 아이템을 선택했을 때
                        break;
                    case R.id.action_three:
                        Intent intent3 = new Intent(ReportHistoryActivity.this, TeamBoardActivity.class);
                        startActivity(intent3);
                        // 팀 홍보 아이템을 선택했을 때
                        break;
                    case R.id.action_four:
                        Intent intent4 = new Intent(ReportHistoryActivity.this, StadiumSelectActivity.class);
                        startActivity(intent4);
                        // 구장정보 아이템을 선택했을 때
                        break;
                    case R.id.action_five:
                        Intent intent5 = new Intent(ReportHistoryActivity.this, MypageActivity.class);
                        startActivity(intent5);
                        // 환경설정 아이템을 선택했을 때
                        break;
                }
                return false;
            }
        });
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        report_recyclerView = findViewById(R.id.reportBoard_Recycler);

        bottomNavigationView.setSelectedItemId(R.id.action_five);
        // 상대매칭 아이템 뷰가 클릭된 상태로 만듬

        report_recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함
        report_manager = new LinearLayoutManager(this);
        report_recyclerView.setLayoutManager(report_manager);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정
        report_arrayList = new ArrayList<>();
        // 신고내역 데이터들을 배열리스트 생성

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성

        report_database = firebaseDatabase.getReference("manager").child("board,comment");
        // report 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = report_database.orderByChild("writetime");
        // 신고가 접수된 시간에 따라 정렬

        query.addListenerForSingleValueEvent(new ValueEventListener() { // manager 키에 일치하는 uid가 있다면
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                report_arrayList.clear(); // 신고내역 데이터들을 담을 배열리스트 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   ReportItem reportItem = snapshot.getValue(ReportItem.class);
                    // reportItem 객체에 데이터를 담음
                    report_arrayList.add(reportItem); // 신고 내역 데이터들을 배열리스트에 추가
                }
                report_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        report_adapter = new ReportHistoryAdapter(report_arrayList, this);
        // 리사이클러뷰에 ReportHistoryAdapter 객체 지정함
        report_recyclerView.setAdapter(report_adapter);
        // 리사이클러뷰에 신고내역 데이터들을 담음
    }
}