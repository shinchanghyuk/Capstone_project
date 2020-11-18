package com.example.capstone_project.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.MainActivity;
import com.example.capstone_project.R;
import com.example.capstone_project.User;
import com.example.capstone_project.mercenary.MercenaryBoardActivity;
import com.example.capstone_project.relative.RelativeBoardActivity;
import com.example.capstone_project.stadium.StadiumSelectActivity;
import com.example.capstone_project.team.TeamBoardActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Arrays;
import java.util.List;

public class MypageActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // 리사이클러뷰 선언
    private RecyclerView.LayoutManager layoutManager; // 레이아웃 매니저 선언
    private RecyclerView.Adapter adapter; // 리사이클러뷰 어댑터 선언
    private ArrayList<MypageItem> arrayList; // 아이템 담을 배열리스트
    BottomNavigationView bottomNavigationView; // 바텀 네이게이션 뷰 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private TextView myinfo_tv; // 로그인 한 사용자를 나타내는 텍스트 뷰들 선언
    private GoogleSignInClient googleSignInClient;
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference relative_database, mercenary_database, team_database,
            comment_database, manager_database, user_database; // 파이버에시스 연결(경로) 선언
    private String loginWay, manager_uid, manager_name, current_uid, current_name; // 쓰이는 문자열 선언

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 바텀 네베게이션 뷰를 클릭 했을 때 동작
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_one:
                        Intent intent1 = new Intent(MypageActivity.this, RelativeBoardActivity.class);
                        startActivity(intent1);
                        // 상대매칭 아이템을 선택했을 때
                        break;
                    case R.id.action_two:
                        Intent intent2 = new Intent(MypageActivity.this, MercenaryBoardActivity.class);
                        startActivity(intent2);
                        // 용병모집 아이템을 선택했을 때
                        break;
                    case R.id.action_three:
                        Intent intent3 = new Intent(MypageActivity.this, TeamBoardActivity.class);
                        startActivity(intent3);
                        // 팀 홍보 아이템을 선택했을 때
                        break;
                    case R.id.action_four:
                        Intent intent4 = new Intent(MypageActivity.this, StadiumSelectActivity.class);
                        startActivity(intent4);
                        // 구장정보 아이템을 선택했을 때
                        break;

                    case R.id.action_five:
                        Intent intent5 = new Intent(MypageActivity.this, MypageActivity.class);
                        startActivity(intent5);
                        // 환경설정 아이템을 선택했을 때
                        break;
                }
                return false;
            }
        });
    }

    private void init() {
        myinfo_tv = findViewById(R.id.myinfo_tv);
        recyclerView = findViewById(R.id.mypage_recyclerview);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.action_five);
        // 내 정보 아이템 뷰가 클릭된 상태로 만듬

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //서버의 클라이언트 ID를 메서드에 전달
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        arrayList = new ArrayList<>();
        // 마이 페이지 메뉴를 담을 배열리스트 생성
        arrayList.clear();
        // 배열리스트 초기화

        recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        auth = FirebaseAuth.getInstance();
        // 파이어베이스 인증 객체 생성
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
        current_name = firebaseUser.getDisplayName();
        current_uid = firebaseUser.getUid();
        // 현재 접속한 사용자의 이름과 uid를 가져옴

        manager_database = firebaseDatabase.getReference("manager");
        // manager 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = manager_database.orderByChild("uid").equalTo(current_uid);
        // manager 테이블에 있는 uid와 현재 사용자의 uid를 비교

        query.addListenerForSingleValueEvent(new ValueEventListener() { // manager 테이블에 일치하는 uid가 있다면
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Manager managerItem = snapshot.getValue(Manager.class);
                    manager_uid = managerItem.getUid();
                    manager_name = managerItem.getName();
                    // 일치하는 사용자의 이름과 uid를 가져옴
                }

                if (manager_uid != null) { // 현재 로그인한 사용자가 관리자라면
                    myinfo_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.manager, 0, 0, 0);
                    myinfo_tv.setText(String.valueOf(" " + manager_name + " 님 환영합니다."));
                    // 옆에 아이콘과 텍스트 변경

                    List<String> listPage = Arrays.asList(getResources().getStringArray(R.array.managerpage));
                    // 관리자 일때 array.xml에 있는 데이터를 가져와 배열 리스트에 값을 넣음

                    for (int i = 0; i < listPage.size(); i++) {
                        MypageItem mypageItem = new MypageItem();
                        mypageItem.setTitle(listPage.get(i));
                        mypageItem.setMoveImage(R.drawable.ic_arrow_forward_black_24dp);
                        arrayList.add(mypageItem);
                        // 관리자 일때 해당하는 데이터와 아이콘을 배열리스트에 추가
                    }

                    adapter = new ManagerpageAdapter(arrayList, MypageActivity.this);
                    // 리사이클러뷰에 ManagerpageAdapter 객체 지정함
                    recyclerView.setAdapter(adapter);
                    // 리사이클러뷰에 관리자일때 해당되는 데이터들을 담음
                    adapter.notifyDataSetChanged();
                    // 리스트 저장 및 새로고침
                } else {
                    user_database = firebaseDatabase.getReference("users");
                    // 해당 게시물 작성자에게 댓글이 달렸다는 알림을 보내기 위한 파이어베이스 경로 설정
                    Query query = user_database.orderByChild("uid").equalTo(current_uid);
                    // users 키에 있는 사용자들의 uid와 현재 사용자의 uid를 비교

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                                User userItem = snapshot.getValue(User.class);
                                loginWay = userItem.getLoginWay();
                                // 로그인 한 방식을 가져옴
                            }
                            if (loginWay.equals("구글")) { // 구글에서 로그인 한 사용자라면
                                myinfo_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.common_google_signin_btn_icon_light, 0, 0, 0);
                                // 사용자를 나타내는 텍스트 뷰에 구글 아이콘을 넣음
                            } else { // 페이스 북에서 로그인 한 사용자라면
                                myinfo_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_facebook_button_icon_blue, 0, 0, 0);
                                // 사용자를 나타내는 텍스트 뷰에 페이스북 아이콘을 넣음
                            }

                            myinfo_tv.setText(" " + current_name + " 님 환영합니다.");
                            // 사용자의 이름을 받아와 텍스트 뷰에 넣음

                            List<String> listPage = Arrays.asList(getResources().getStringArray(R.array.mypage));
                            // 일반 사용자 일때 array.xml에 있는 데이터를 가져와 배열리스트에 값을 넣음

                            for (int i = 0; i < listPage.size(); i++) {
                                MypageItem mypageItem = new MypageItem();
                                mypageItem.setTitle(listPage.get(i));
                                mypageItem.setMoveImage(R.drawable.ic_arrow_forward_black_24dp);
                                arrayList.add(mypageItem);
                                // 일반 사용자 일때 해당하는 데이터와 아이콘을 배열리스트에 추가
                            }

                            adapter = new MypageAdapter(arrayList, MypageActivity.this);
                            // MypageAdapter 객체를 생성
                            recyclerView.setAdapter(adapter);
                            // 리사이클러뷰에 일반 사용자 일때 해당되는 데이터들을 담음
                            adapter.notifyDataSetChanged();
                            // 리스트 저장 및 새로고침
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
    }

    // 리사이클러뷰 목록에서 로그아웃을 눌렀을 때 동작
    public void logout() {
        if (current_uid != null) { // 현재 사용자의 uid가 안비어 있을 때
            auth.signOut(); // 파이어베이스 인증객체 로그아웃
            LoginManager.getInstance().logOut();
            // 사용자가 페이스북 로그인 시 로그아웃 동작

            googleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "로그아웃이 성공적으로 마무리되었습니다.", Toast.LENGTH_SHORT).show();
                            // 로그아웃 후 MainActivity로 이동 및 Toast 메세지 전송
                        }
                    });
            // 사용자가 구글 로그인 시 로그아웃 동작
        }
    }

    // 리사이클러뷰 목록에서 회원탈퇴를 눌렀을 때 동작
    public void userWithdrawal() {
        auth.signOut(); // 파이어베이스 인증객체 로그아웃
        LoginManager.getInstance().logOut();
        // 사용자가 페이스북 로그인 시 로그아웃 동작

        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        relative_database = firebaseDatabase.getReference("board").child("relative");
                        // relative 키에 접근하기 위한 파이어베이스 경로 설정
                        Query query = relative_database.orderByChild("uid").equalTo(current_uid);
                        // relative 키에 있는 게시글들을 작성한 사용자의 uid와 지금 현재 사용자의 uid를 비교
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                    // 해당 유저가 작성한 상대매칭 게시물 데이터 삭제
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        mercenary_database = firebaseDatabase.getReference("board").child("mercenary");
                        // mercenary 키에 접근하기 위한 파이어베이스 경로 설정
                        Query query2 = mercenary_database.orderByChild("uid").equalTo(current_uid);
                        // mercenary 키에 있는 게시글들을 작성한 사용자의 uid와 지금 현재 사용자의 uid를 비교
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                    // 해당 유저가 작성한 용병모집 게시물 데이터 삭제
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        team_database = firebaseDatabase.getReference("board").child("team");
                        // team 키에 접근하기 위한 파이어베이스 경로 설정
                        Query query3 = team_database.orderByChild("uid").equalTo(current_uid);
                        // team 키에 있는 게시글들을 작성한 사용자의 uid와 지금 현재 사용자의 uid를 비교
                        query3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                    // 해당 유저가 작성한 팀 홍보 게시물 데이터 삭제
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        comment_database = firebaseDatabase.getReference("board").child("comment");
                        // comment 키에 접근하기 위한 파이어베이스 경로 설정
                        Query query4 = comment_database.orderByChild("uid").equalTo(current_uid);
                        // comment 키에 있는 댓글을 작성한 사용자의 uid와 지금 현재 사용자의 uid를 비교

                        query4.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                    // 해당 유저가 작성한 댓글 데이터 삭제
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        user_database = firebaseDatabase.getReference("users").child(current_uid);
                        // users 키에 접근하기 위한 파이어베이스 경로 설정
                        user_database.removeValue();
                        // users 키에 있는 현재 사용자의 데이터를 삭제

                        Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "회원탈퇴가 성공적으로 마무리되었습니다.", Toast.LENGTH_SHORT).show();
                        // 삭제 후 MainActivity로 이동되며, Toast 메세지 전송
                    }
                });
    }
}
