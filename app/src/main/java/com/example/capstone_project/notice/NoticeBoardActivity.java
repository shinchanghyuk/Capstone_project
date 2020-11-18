package com.example.capstone_project.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;
import com.example.capstone_project.SpinnerAdapter;
import com.example.capstone_project.mercenary.MercenaryBoardActivity;
import com.example.capstone_project.mypage.Manager;
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
import java.util.Collections;

public class NoticeBoardActivity extends AppCompatActivity {
    private ImageButton write_btn, search_btn; // 버튼들 선언
    private RecyclerView recyclerView; // 리사이클러뷰 선언
    private RecyclerView.LayoutManager layoutManager;   // 레이아웃 매니저 선언
    private RecyclerView.Adapter search_adapter, notice_adapter; // 리사이클러뷰 어댑터 선언
    private ArrayList<Manager> search_arrayList, notice_arrayList; // 아이템 담을 배열리스트 선언
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference notice_database, manager_database; // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private String sort, sort_search, current_uid, manager_uid; // 쓰이는 문자열 선언
    private String[] spinnerSearch; // 검색 스피너 목록들을 담을 문자열 배열선언
    private Spinner search_spinner; // 검색 스피너 선언
    private EditText search_edit; // 사용자가 검색하고자 하는 내용
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board);
        start(); // 관리자 모드일 때 글쓰기 버튼과 알림 버튼을 안보이게 함
        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 검색 스피너를 눌렀을 때 동작
        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = (String) parent.getItemAtPosition(position);
                // 사용자가 선택한 스피너의 값을 가져옴
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 검색 버튼을 눌렀을 때 동작
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_search = search_edit.getText().toString(); // 사용자가 입력한 검색 내용

                if (sort.equals("검색") || sort_search.isEmpty()) { // 키워드 설정 및 검색 내용이 비어있다면 Toast 메세지 전송
                    Toast.makeText(getApplicationContext(), "검색조건을 다시 설정해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    notice_database.orderByChild("writetime").addListenerForSingleValueEvent(new ValueEventListener() {
                        // 게시물 작성 시간으로 정렬
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            search_arrayList.clear(); // 사용자의 검색조건에 해당하는 데이터들을 담을 배열리스트 초기화
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                                Manager managerItem = snapshot.getValue(Manager.class);
                                // MercenaryBoardItem 객체 재활용
                                if (sort.equals("제목")) { // 제목으로 검색할 때
                                    if (managerItem.getTitle().contains(sort_search)) {
                                        search_arrayList.add(managerItem);
                                        // 사용자가 입력한 내용이 데이터베이스에 포함되어 있을 때 배열리스트에 추가
                                    }
                                }
                                if (search_arrayList.size() == 0) { // 사용자의 검색 조건에 해당하는 게시물이 없을 때
                                    Toast.makeText(getApplicationContext(), "원하시는 조건의 게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    recyclerView.setAdapter(notice_adapter); // 리사이클러뷰에 공지사항 게시판에 등록 된 데이터들을 담음
                                    notice_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                                } else {
                                    Collections.reverse(search_arrayList); // 배열리스트를 내림차순으로 정렬
                                    search_edit.setText(null); // 검색 내용 초기화
                                    recyclerView.setAdapter(search_adapter); // 리사이클러뷰에 사용자의 검색 조건에 해당하는 데이터들을 담음
                                    search_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                                    Toast.makeText(getApplicationContext(), search_arrayList.size() + "건의 게시물을 찾았습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        // 글쓰기 버튼을 눌렀을 때 동작
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeBoardActivity.this, NoticeWritingActivity.class);
                startActivity(intent);
                // 공지사항 글쓰는 화면으로 이동
            }
        });

        // 바텀 네베게이션 뷰를 클릭 했을 때 동작
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_one:
                        Intent intent1 = new Intent(NoticeBoardActivity.this, RelativeBoardActivity.class);
                        startActivity(intent1);
                        // 상대매칭 아이템을 선택했을 때
                        break;
                    case R.id.action_two:
                        Intent intent2 = new Intent(NoticeBoardActivity.this, MercenaryBoardActivity.class);
                        startActivity(intent2);
                        // 용병모집 아이템을 선택했을 때
                        break;
                    case R.id.action_three:
                        Intent intent3 = new Intent(NoticeBoardActivity.this, TeamBoardActivity.class);
                        startActivity(intent3);
                        // 팀 홍보 아이템을 선택했을 때
                        break;
                    case R.id.action_four:
                        Intent intent4 = new Intent(NoticeBoardActivity.this, StadiumSelectActivity.class);
                        startActivity(intent4);
                        // 구장정보 아이템을 선택했을 때
                        break;
                    case R.id.action_five:
                        Intent intent5 = new Intent(NoticeBoardActivity.this, MypageActivity.class);
                        startActivity(intent5);
                        // 환경설정 아이템을 선택했을 때
                        break;
                }
                return false;
            }
        });
    }

    private void start() {
        write_btn = findViewById(R.id.write_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        auth = FirebaseAuth.getInstance();
        // 파이어베이스 인증 객체 생성
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
        current_uid = firebaseUser.getUid();
        // 현재 접속한 사용자의 uid를 가져옴

        manager_database = firebaseDatabase.getReference("manager");
        // manager 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = manager_database.orderByChild("uid").equalTo(current_uid);
        // manager 키에 있는 uid와 현재 사용자의 uid를 비교

        query.addListenerForSingleValueEvent(new ValueEventListener() { // manager 키에 일치하는 uid가 있다면
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Manager managerItem = snapshot.getValue(Manager.class);
                    manager_uid = managerItem.getUid();
                    // 일치하는 사용자의 uid를 가져옴
                }

                if (manager_uid != null) { // 현재 로그인한 사용자가 관리자라면
                    write_btn.setVisibility(View.VISIBLE);
                    // 글쓰기 버튼을 보이게 함
                } else {
                    write_btn.setVisibility(View.INVISIBLE);
                    // 글쓰기 버튼을 안보이게 함
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        search_spinner = findViewById(R.id.search_spinner);
        search_edit = findViewById(R.id.search_edit);
        search_btn = findViewById(R.id.search_btn);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        recyclerView = findViewById(R.id.noticeBoard_Recycler);

        bottomNavigationView.setSelectedItemId(R.id.action_five);
        // 내 정보 아이템 뷰가 클릭된 상태로 만듬

        recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        search_arrayList = new ArrayList<>();
        // 사용자의 검색조건에 해당되는 데이터들을 담을 배열 리스트 생성
        notice_arrayList = new ArrayList<>();
        // 공지사항 게시판에 데이터들을 담을 배열리스트 생성

        notice_database = firebaseDatabase.getReference("board").child("notice");
        // notice 키 값에 접근하기 위한 파이어베이스 경로 설정
        Query query = notice_database.orderByChild("writetime"); // 관리자가 게시물을 작성한 시간으로 정렬

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notice_arrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                    Manager managerItem = snapshot.getValue(Manager.class);
                    // manangerItem 객체에 데이터를 담음
                    notice_arrayList.add(managerItem); // 모든 공지사항 게시물의 데이터들을 배열리스트에 추가

                }
                Collections.reverse(notice_arrayList); // 배열리스트 내림차순으로 정렬
                notice_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        notice_adapter = new NoticeBoardAdapter(notice_arrayList, this);
        search_adapter = new NoticeBoardAdapter(search_arrayList, this);
        // NoticeBoardAdapter 객체들을 생성

        recyclerView.setAdapter(notice_adapter);
        // 리사이클러뷰에 공지사항 게시판에 등록 된 데이터들을 담음

        spinnerSearch = getResources().getStringArray(R.array.notice_search);
        // array.xml에 있는 데이터를 가져와 문자열 배열 변수에 값을 넣음

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(spinnerSearch, this);
        search_spinner.setAdapter(spinnerAdapter);
        // spinnerAdapter 객체를 생성해 검색 스피너에 담음
    }
}
