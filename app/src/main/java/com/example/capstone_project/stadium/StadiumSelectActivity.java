package com.example.capstone_project.stadium;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.MenuActivity;
import com.example.capstone_project.R;
import com.example.capstone_project.SpinnerAdapter;
import com.example.capstone_project.mercenary.MercenaryBoardActivity;
import com.example.capstone_project.mypage.MypageActivity;
import com.example.capstone_project.relative.RelativeBoardActivity;
import com.example.capstone_project.team.TeamBoardActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class StadiumSelectActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageButton search_btn; // 버튼들 선언
    private RecyclerView recyclerView; // 리사이클러뷰 선언
    private Spinner search_spinner; // 검색 스피너 선언
    private RecyclerView.LayoutManager layoutManager; // 레이아웃 매니저 선언
    private RecyclerView.Adapter search_adapter; // 리사이클러뷰 어댑터 선언
    private ArrayList<StadiumItem> search_arrayList; // 아이템 담을 배열리스트 선언
    private EditText search_edit; // 사용자의 검색 내용을 담을 edit 선언
    private String[] spinnerSearch; // 검색 스피너 목록들을 담을 문자열 배열선언
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰 선언
    private String sort, sort_search, stadium_name; // 쓰이는 문자열 선언
    private Float latitude, longitude;
    private MapFragment mapFrag;
    private GoogleMap gMap;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference stadium_database; // 파이버에시스 연결(경로) 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_select);
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
                    stadium_database.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        // 사용자의 검색으로 나온 데이터들을 이름 순으로 정렬
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            search_arrayList.clear(); // 사용자의 검색조건에 해당하는 데이터들을 담을 배열리스트 초기화
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                                StadiumItem stadiumItem = snapshot.getValue(StadiumItem.class);
                                if (sort.equals("지역")) { // 지역으로 검색할 때
                                    if (stadiumItem.getGu().contains(sort_search)) {
                                        search_arrayList.add(stadiumItem);
                                        // 사용자가 입력한 내용이 데이터베이스에 포함되어 있을 때 배열리스트에 추가
                                    }
                                } else if (sort.equals("장소")) { // 제목으로 검색할 때
                                    if (stadiumItem.getName().contains(sort_search)) {
                                        search_arrayList.add(stadiumItem);
                                        // 사용자가 입력한 내용이 데이터베이스에 포함되어 있을 때 배열리스트에 추가
                                    }
                                }
                            }

                            if (search_arrayList.size() == 0) { // 사용자의 검색 조건에 해당하는 경기장이 없을 때
                                Toast.makeText(getApplicationContext(), "원하시는 조건의 경기장 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Collections.reverse(search_arrayList); // 배열리스트를 내림차순으로 정렬
                                search_edit.setText(null); // 검색 내용 초기화
                                recyclerView.setAdapter(search_adapter); // 리사이클러뷰에 사용자의 검색 조건에 해당하는 데이터들을 담음
                                search_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                                Toast.makeText(getApplicationContext(), search_arrayList.size() + "건의 경기장 정보를 찾았습니다.", Toast.LENGTH_SHORT).show();
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

        // 바텀 네베게이션 뷰를 클릭 했을 때 동작
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_one:
                        Intent intent1 = new Intent(StadiumSelectActivity.this, RelativeBoardActivity.class);
                        startActivity(intent1);
                        // 상대매칭 아이템을 선택했을 때
                        break;
                    case R.id.action_two:
                        Intent intent2 = new Intent(StadiumSelectActivity.this, MercenaryBoardActivity.class);
                        startActivity(intent2);
                        // 용병모집 아이템을 선택했을 때
                        break;
                    case R.id.action_three:
                        Intent intent3 = new Intent(StadiumSelectActivity.this, TeamBoardActivity.class);
                        startActivity(intent3);
                        // 팀 홍보 아이템을 선택했을 때
                        break;
                    case R.id.action_four:
                        Intent intent4 = new Intent(StadiumSelectActivity.this, StadiumSelectActivity.class);
                        startActivity(intent4);
                        // 구장정보 아이템을 선택했을 때
                        break;
                    case R.id.action_five:
                        Intent intent5 = new Intent(StadiumSelectActivity.this, MypageActivity.class);
                        startActivity(intent5);
                        // 환경설정 아이템을 선택했을 때
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap; // 구글 맵 변수를 넣음

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.568256, 126.897240),15));
        // 구글맵 카메라 초기값 설정
        gMap.getUiSettings().setZoomControlsEnabled(true);
        // ?

        // stadium에 있는 모든 경기장를 토대로 마커를 설정함
        stadium_database.addListenerForSingleValueEvent(new ValueEventListener() { // stadium 키에 있는 경기장 접근
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                    StadiumItem stadiumItem = snapshot.getValue(StadiumItem.class);
                    latitude = stadiumItem.getLatitude(); // 경기장의 위도를 변수에 넣음
                    longitude = stadiumItem.getLongitude(); // 경기장의 경도를 변수에 넣음

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitude, longitude)); // 해당 경기장의 위도, 경도에 따라 마커 설정
                    markerOptions.alpha(0.7f); //
                    gMap.addMarker(markerOptions); // 마커 설정 이후 출력
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        // 마커를 클릭하였을 때 동작
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(StadiumSelectActivity.this, StadiumDetailsActivity.class);
                intent.putExtra("stadium_name", stadium_name);
                startActivity(intent);
                // 사용자가 정보를 알고 싶은 경기장의 이름를 담고 경기장의 세부정보를 보여주는 화면으로 이동
                return false;

            }
        });
    }

    public void init() {
        search_btn = findViewById(R.id.search_btn);
        search_spinner = findViewById(R.id.search_spinner);
        search_edit = findViewById(R.id.search_edit);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        recyclerView = findViewById(R.id.stadium_Recycler);

        bottomNavigationView.setSelectedItemId(R.id.action_four);
        // 상대매칭 아이템 뷰가 클릭된 상태로 만듬

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MODE_PRIVATE);
        // 퍼미션 요청

        recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        search_arrayList = new ArrayList<>();
        // 사용자의 검색조건에 해당되는 경기장 데이터들을 담을 배열 리스트 생성
        search_adapter = new StadiumAdapter(search_arrayList, this);
        // StadiumAdapter 객체들을 생성
        spinnerSearch = getResources().getStringArray(R.array.stadium_search);
        // array.xml에 있는 데이터들을 가져와 문자열 배열 변수에 값을 넣음

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(spinnerSearch, this);
        search_spinner.setAdapter(spinnerAdapter);
        // spinnerAdapter 객체를 생성해 검색 스피너에 담음

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
       recyclerView.addItemDecoration(dividerItemDecoration);
        // 리사이클러뷰 목록을 구별하기 위해 밑선을 추가

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        stadium_database = firebaseDatabase.getReference("stadium");
        // 해당 게시물을 relative 키에 새로 넣기 위한 파이어베이스 경로 설정

        mapFrag =  (MapFragment) getFragmentManager().findFragmentById(R.id.stadium_map);
        mapFrag.getMapAsync(this);
    }
    public void map(String stadium_name) {
        this.stadium_name = stadium_name; // 사용자가 선택한 경기장 이름을 변수에 담음

        Query query = stadium_database.orderByChild("name").equalTo(stadium_name);
        // stadium 키에 있는 name과 사용자가 선택한 경기장의 이름을 비교
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                    StadiumItem stadiumItem = snapshot.getValue(StadiumItem.class);
                    longitude = stadiumItem.getLongitude(); // 해당 경기장의 경도를 가져와 변수에 넣음
                    latitude = stadiumItem.getLatitude(); // 해당 경기장의 위도를 가져와 변수에 넣음
                }

                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15));
                // 해당 경기장의 위도, 경도로 카메라 설정
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    // 뒤로 가기 이벤트가 발생되었을 때 동작
    public void onBackPressed() {
        Intent intent = new Intent(StadiumSelectActivity.this, MenuActivity.class);
        startActivity(intent);
        // 메뉴 화면으로 이동
    }
}

