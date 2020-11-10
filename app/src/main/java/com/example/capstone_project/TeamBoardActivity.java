package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TeamBoardActivity extends AppCompatActivity {
    private Button search_btn, write_btn;
    private Spinner search_spinner;
    BottomNavigationView bottomNavigationView;  // 바텀 네이게이션 뷰 선언
    private RecyclerView recyclerView;  // 리사이클러뷰 선언
    private RecyclerView.LayoutManager layoutManager;   // 레이아웃 매니저 선언
    private RecyclerView.Adapter adapter;   // 리사이클러뷰 어댑터 선언
    private ArrayList<TeamBoardItem> arrayList; // 아이템 담을 배열리스트 선언
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;    // 파이버에시스 연결(경로) 선언
    private String sort, sort_standard, sort_search, key, matching, day,
            yesterDay;
    private EditText search_edit; // 사용자가 검색하고자 하는 내용
    private String[] spinnerSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_board);

        init();

        // 검색 스피너를 눌렀을 때 동작
        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 글쓰기 버튼을 눌렀을 때 동작
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamBoardActivity.this, TeamWritingActivity.class);
                startActivity(intent);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_search = search_edit.getText().toString();

                if (sort.equals("지역")) {
                    sort_standard = "place";
                } else if (sort.equals("작성자")) {
                    sort_standard = "user";
                }

                databaseReference.orderByChild(sort_standard).equalTo(sort_search).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                            TeamBoardItem teamBoardItem = snapshot.getValue(TeamBoardItem.class);
                            // RelativeBoardItem 객체에 데이터를 담음
                            arrayList.add(teamBoardItem);
                        }
                        if (arrayList.size() == 0) {
                            Toast.makeText(getApplicationContext(), "원하시는 조건의 게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            init();
                        } else {
                            adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                            Toast.makeText(getApplicationContext(), arrayList.size() + "건의 게시물을 찾았습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_one:
                        Intent intent1 = new Intent(TeamBoardActivity.this, RelativeBoardActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_two:
                        Intent intent2 = new Intent(TeamBoardActivity.this, MercenaryBoardActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_three:
                        Intent intent3 = new Intent(TeamBoardActivity.this, TeamBoardActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_four:
                        Intent intent4 = new Intent(TeamBoardActivity.this, StadiumSelectActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.action_five:
                        Intent intent5 = new Intent(TeamBoardActivity.this, MypageActivity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        });

    }

    private void init() {
        search_spinner = findViewById(R.id.search_spinner);
        search_edit = findViewById(R.id.search_edit);
        write_btn = findViewById(R.id.write_btn);
        search_btn = findViewById(R.id.search_btn);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        mDate = new Date(mDate.getTime() + (1000 * 60 * 60 * 24 * -1));

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");

        yesterDay = simpleDate.format(mDate); // 어제 날짜를 받아와 yesterDay 변수에 넣음

        recyclerView = findViewById(R.id.teamBoard_Recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("team");

        Query query = databaseReference.orderByChild("day"); // 모집일자에 따라 정렬

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    TeamBoardItem teamBoardItem = snapshot.getValue(TeamBoardItem.class);
                    // RelativeBoardItem 객체에 데이터를 담음
                    key = snapshot.getKey();
                    matching = teamBoardItem.getMatching();
                    day = teamBoardItem.getDay();
                    arrayList.add(teamBoardItem);

                    adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

                    int compare = day.compareTo(yesterDay);

                    if (compare > 0) {
                        matching = "모집 중";
                    } else if (compare <= 0) {
                        matching = "모집완료";
                    }

                    databaseReference2 = firebaseDatabase.getReference("board").child("team").child(key);
                    Map<String, Object> matchingChange = new HashMap<>();
                    matchingChange.put("matching", matching);
                    databaseReference2.updateChildren(matchingChange);
                }   // 해당 게시물의 매칭현황을 변경
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
        adapter = new TeamBoardAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        spinnerSearch = getResources().getStringArray(R.array.teamboard_search);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(spinnerSearch, this);
        search_spinner.setAdapter(spinnerAdapter);

        bottomNavigationView.setSelectedItemId(R.id.action_three);
    }
}
