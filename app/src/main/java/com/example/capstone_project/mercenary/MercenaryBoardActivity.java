package com.example.capstone_project.mercenary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.MenuActivity;
import com.example.capstone_project.R;
import com.example.capstone_project.SpinnerAdapter;
import com.example.capstone_project.User;
import com.example.capstone_project.alarm.AlarmActivity;
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

public class MercenaryBoardActivity extends AppCompatActivity {
    private ImageButton search_btn, write_btn, alarm_btn; // 버튼들 선언
    private Spinner search_spinner; // 검색 스피너 선언
    private RecyclerView recyclerView; // 리사이클러뷰 선언
    private RecyclerView.LayoutManager layoutManager; // 레이아웃 매니저 선언
    private RecyclerView.Adapter mercenary_adapter, search_adapter; // 리사이클러뷰 어댑터 선언
    private ArrayList<MercenaryBoardItem> search_arrayList, mercenary_arrayList; // 아이템 담을 배열리스트 선언
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference mercenary_database, user_database, manager_database; // 파이버에시스 연결(경로) 선언
    private String sort, sort_search, type, current_uid, mealarm, manager_uid; // 쓰이는 문자열 선언
    private EditText search_edit; // 사용자의 검색 내용을 담을 edit 선언
    private String[] spinnerSearch; // 검색 스피너 목록들을 담을 문자열 배열선언
    private RadioGroup type_radio; // 라디오 그룹 변수 선언
    private RadioButton mercenary_radio, team_radio; // 용병, 팀 라디오버튼 변수 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private BottomNavigationView bottomNavigationView; // 바텀 네이게이션 뷰 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mercenary_board);

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

        // 글쓰기 버튼을 눌렀을 때 동작
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MercenaryBoardActivity.this, MercenaryWritingActivity.class);
                startActivity(intent);
                // 글쓰는 화면으로 이동
            }
        });

        // 알람 버튼을 눌렀을 때 동작
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_database = firebaseDatabase.getReference("users");
                // users 키에 접근하기 위한 파이어베이스 경로 설정
                Query query = user_database.orderByChild("uid").equalTo(current_uid);
                // users 키에 있는 uid와 현재 사용자의 uid를 비교

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // 반복문으로 데이터리스트를 추출
                            User userItem = snapshot.getValue(User.class);
                            mealarm = userItem.getMealarm();
                            // 데이터베이스에서 현재 사용자의 mealarm 값을 가져옴
                        }

                        if (mealarm.equals("o")) { // 이 사용자가 상대매칭 알림을 받는 사용자 일때(mealarm 값이 o일 때)
                            Intent intent = new Intent(MercenaryBoardActivity.this, AlarmActivity.class);
                            intent.putExtra("number", "2");
                            startActivity(intent);
                            // 알림 여부 설정 화면으로 이동
                        } else {
                            Toast.makeText(getApplicationContext(), "알림여부를 허용하시기 바랍니다.", Toast.LENGTH_LONG).show();
                            // 알림을 받는 사용자가 아닐 시 Toast 메세지 전송
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
                    mercenary_database.orderByChild("writetime").addListenerForSingleValueEvent(new ValueEventListener() {
                        // 게시물 작성 시간으로 정렬
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        search_arrayList.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                            MercenaryBoardItem mercenaryBoardItem = snapshot.getValue(MercenaryBoardItem.class);
                            if (sort.equals("지역")) { // 지역으로 검색할 때
                                if (mercenaryBoardItem.getPlace().contains(sort_search)) {
                                    search_arrayList.add(mercenaryBoardItem);
                                    // 사용자가 입력한 내용이 데이터베이스에 포함되어 있을 때 배열리스트에 추가
                                }
                            } else if (sort.equals("제목")) { // 제목으로 검색할 때
                                if (mercenaryBoardItem .getTitle().contains(sort_search)) {
                                    search_arrayList.add(mercenaryBoardItem);
                                    // 사용자가 입력한 내용이 데이터베이스에 포함되어 있을 때 배열리스트에 추가
                                }
                            } else if (sort.equals("일자")) { // 일자로 검색할 때
                                if (mercenaryBoardItem.getDay().contains(sort_search)) {
                                    search_arrayList.add(mercenaryBoardItem);
                                    // 사용자가 입력한 내용이 데이터베이스에 포함되어 있을 때 배열리스트에 추가
                                }
                            } else if (sort.equals("작성자")) { // 작성자로 검색할 때
                                if (mercenaryBoardItem.getUser().contains(sort_search)) {
                                    search_arrayList.add(mercenaryBoardItem);
                                    // 사용자가 입력한 내용이 데이터베이스에 포함되어 있을 때 배열리스트에 추가
                                }
                            }
                            Collections.reverse(search_arrayList); // 배열리스트를 내림차순으로 정렬
                            search_edit.setText(null);
                        }
                            if (search_arrayList.size() == 0) { // 사용자의 검색 조건에 해당하는 게시물이 없을 때
                                Toast.makeText(getApplicationContext(), "원하시는 조건의 게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                recyclerView.setAdapter(mercenary_adapter); // 리사이클러뷰에 용병모집 게시판에 등록 된 데이터들을 담음
                                mercenary_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                            } else {
                                Collections.reverse(search_arrayList); // 배열리스트를 내림차순으로 정렬
                                search_edit.setText(null); // 검색 내용 초기화
                                recyclerView.setAdapter(search_adapter); // 리사이클러뷰에 사용자의 검색 조건에 해당하는 데이터들을 담음
                                search_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                                Toast.makeText(getApplicationContext(), search_arrayList.size() + "건의 게시물을 찾았습니다.", Toast.LENGTH_SHORT).show();
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

        // 용병 또는 팀 라디오 버튼을 눌렀을 때 동작
        type_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio1) {
                    type = "용병";
                } else if (checkedId == R.id.radio2) {
                    type = "팀";
                    // 해당 되는 문자열을 변수에 담음
                }

               mercenary_database.orderByChild("type").equalTo(type).addListenerForSingleValueEvent(new ValueEventListener() {
                   // 용병모집 게시판에 있는 게시글 중 해당 type과 사용자가 선택한 type을 비교
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        search_arrayList.clear(); // 사용자가 선택한 타입과 일치하는 데이터들을 담을 배열리스트 초기화

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                            MercenaryBoardItem mercenaryBoardItem = snapshot.getValue(MercenaryBoardItem.class);
                            // MercenaryBoardItem 객체 재활용
                            search_arrayList.add(mercenaryBoardItem);
                            // 사용자가 선택한 type에 해당하는 게시물이 데이터베이스에 포함되어 있을 때 배열리스트에 추가
                        }
                        if (search_arrayList.size() == 0) { // 사용자가 선택한 type에 해당하는 게시물이 없을 때
                            Toast.makeText(getApplicationContext(), "원하시는 조건의 게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            recyclerView.setAdapter(mercenary_adapter); // 리사이클러뷰에 용병모집 게시판에 등록 된 데이터들을 담음
                            mercenary_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                        } else {
                            Collections.reverse(search_arrayList); // 배열리스트를 내림차순으로 정렬
                            recyclerView.setAdapter(search_adapter); // 리사이클러뷰에 사용자의 검색 조건에 해당하는 데이터들을 담음
                            search_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                            Toast.makeText(getApplicationContext(), search_arrayList.size() + "건의 게시물을 찾았습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // 바텀 네베게이션 뷰를 클릭 했을 때 동작
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_one:
                        Intent intent1 = new Intent(MercenaryBoardActivity.this, RelativeBoardActivity.class);
                        startActivity(intent1);
                        // 상대매칭 아이템을 선택했을 때
                        break;
                    case R.id.action_two:
                        Intent intent2 = new Intent(MercenaryBoardActivity.this, MercenaryBoardActivity.class);
                        startActivity(intent2);
                        // 용병모집 아이템을 선택했을 때
                        break;
                    case R.id.action_three:
                        Intent intent3 = new Intent(MercenaryBoardActivity.this, TeamBoardActivity.class);
                        startActivity(intent3);
                        // 팀 홍보 아이템을 선택했을 때
                        break;
                    case R.id.action_four:
                        Intent intent4 = new Intent(MercenaryBoardActivity.this, StadiumSelectActivity.class);
                        startActivity(intent4);
                        // 구장정보 아이템을 선택했을 때
                        break;
                    case R.id.action_five:
                        Intent intent5 = new Intent(MercenaryBoardActivity.this, MypageActivity.class);
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
        alarm_btn = findViewById(R.id.alarm_btn);

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
                    write_btn.setVisibility(View.INVISIBLE);
                    alarm_btn.setVisibility(View.INVISIBLE);
                    // 글쓰기, 알림 버튼을 안보이게 함
                } else { // 현재 로그인한 사용자가 일반 사용자라면
                    write_btn.setVisibility(View.VISIBLE);
                    alarm_btn.setVisibility(View.VISIBLE);
                    // 일반 사용자 일때 글쓰기, 알림 버튼을 보이게 함
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
        type_radio = findViewById(R.id.type_radio);
        mercenary_radio = findViewById(R.id.radio1);
        team_radio = findViewById(R.id.radio2);
        recyclerView = findViewById(R.id.mercenaryBoard_Recycler);

        recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        search_arrayList = new ArrayList<>();
        // 사용자의 검색조건에 해당되는 데이터들을 담을 배열 리스트 생성
        mercenary_arrayList = new ArrayList<>();
        // 용병모집 게시판에 데이터들을 담을 배열리스트 생성

        bottomNavigationView.setSelectedItemId(R.id.action_two);
        // 용병모집 아이템 뷰가 클릭된 상태로 만듬

        mercenary_database = firebaseDatabase.getReference("board").child("mercenary");
        // mercenary 키 값에 접근하기 위한 파이어베이스 경로 설정
        Query query = mercenary_database.orderByChild("writetime"); // 사용자가 게시물을 작성한 시간으로 정렬

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mercenary_arrayList.clear(); // 용병모집 게시판에 등록된 데이터들을 담을 배열리스트 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    MercenaryBoardItem mercenaryBoardItem = snapshot.getValue(MercenaryBoardItem.class);
                    // mercenaryBoardItem 객체에 데이터를 담음
                    mercenary_arrayList.add(mercenaryBoardItem);
                }
                Collections.reverse(mercenary_arrayList); // 배열리스트 내림차순으로 정렬
                mercenary_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        mercenary_adapter = new MercenaryBoardAdapter(mercenary_arrayList, this);
        search_adapter = new MercenaryBoardAdapter(search_arrayList, this);
        // MeBoardAdapter 객체들을 생성

        recyclerView.setAdapter(mercenary_adapter);
        // 리사이클러뷰에 용병모집 게시판에 등록 된 데이터들을 담음

        spinnerSearch = getResources().getStringArray(R.array.board_search);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(spinnerSearch, this);
        search_spinner.setAdapter(spinnerAdapter);
        // 검색 스피너 생성
    }
    @Override
    // 뒤로 가기 이벤트가 발생되었을 때 동작
    public void onBackPressed() {
        Intent intent = new Intent(MercenaryBoardActivity.this, MenuActivity.class);
        startActivity(intent);
        // 메뉴 화면으로 이동
    }
}