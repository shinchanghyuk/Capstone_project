package com.example.capstone_project.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.capstone_project.R;
import com.example.capstone_project.User;
import com.example.capstone_project.mypage.MypageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AlarmSettingActivity extends AppCompatActivity {
    private SwitchCompat relativeSwitch, mercenarySwitch, noticeSwitch; // 상대매칭, 용병모집, 공지사항 스위치들 선언
    private Button confirm_btn; // 버튼 선언
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference user_database, user_database2; // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private String current_user, current_uid, realarm, mealarm, noticealarm; // 쓰이는 문자열 선언
    public static String userToken, loginWay, redate, replace, medate, meplace;
    // 사용자의 유저토큰, 로그인 경로, 상대매칭과 용병모집의 알림지역, 일자들을 담을 정적변수 선언

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 상대매칭 게시판 스위치를 눌렀을 때 동작
        relativeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    realarm = "o";
                } else {
                    realarm = "x";
                }
            }
        });

        // 용병모집 게시판 스위치를 눌렀을 때 동작
        mercenarySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mealarm = "o";
                } else {
                    mealarm = "x";
                }
            }
        });

        // 공지사항 게시판 스위치를 눌렀을 때 동작
        noticeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    noticealarm = "o";
                } else {
                    noticealarm = "x";
                }
            }
        });

        // 확인 버튼을 눌렀을 때 동작
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realarm.equals("x")) { // 상대매칭 알람 여부를 꺼놨을 때
                    redate = ""; // 상대매칭 알림일자 변수 초기화
                    replace = ""; // 상대매칭 알림지역 변수 초기화
                }

                if (mealarm.equals("x")) { // 용병모집 알람 여부를 꺼놨을 때
                    medate = ""; // 용병모집 알림일자 변수 초기화
                    meplace = ""; // 용병모집 알림지역 변수 초기화
                }
                user_database2 = user_database.child(current_uid);
                // 현재 사용자의 users 키에 접근하기 위한 파이어베이스 경로 설정

                User user = new User(current_user, current_uid, loginWay, userToken, realarm, mealarm, replace, redate, meplace, medate, noticealarm);
                user_database2.setValue(user);
                // 해당 유저의 알림 설정 파이어베이스 업로드 구문

                Toast.makeText(AlarmSettingActivity.this, "알람여부를 설정하였습니다.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송
                Intent intent = new Intent(AlarmSettingActivity.this, MypageActivity.class);
                startActivity(intent);
                // MypageActivity로 이동
            }
        });
    }
    private void init() {
        confirm_btn = findViewById(R.id.confirm_btn);
        relativeSwitch = findViewById(R.id.relativeSwitch);
        mercenarySwitch = findViewById(R.id.mercenarySwitch);
        noticeSwitch = findViewById(R.id.noticeSwitch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        auth = FirebaseAuth.getInstance();
        // 파이어베이스 인증 객체 생성
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
        current_user = firebaseUser.getDisplayName();
        current_uid = firebaseUser.getUid();
        // 현재 접속한 사용자의 uid와 이름을 가져옴

        user_database = firebaseDatabase.getReference("users");
        // user 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = user_database.orderByChild("uid").equalTo(current_uid);
        // users 키에 있는 사용자들의 uid와 현재 사용자의 uid를 비교

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                    User userItem = snapshot.getValue(User.class);
                    loginWay = userItem.getLoginWay();
                    userToken = userItem.getUserToken();
                    realarm = userItem.getRealarm();
                    mealarm = userItem.getMealarm();
                    redate = userItem.getRedate();
                    replace = userItem.getReplace();
                    medate = userItem.getMedate();
                    meplace = userItem.getMeplace();
                    noticealarm = userItem.getNoticealarm();
                    // 데이터베이스에서 일치하는 사용자의 정보들을 가져옴
                }

                if (realarm.equals("o")) { // 사용자가 상대매칭 알람 여부를 켜놨을 때
                    relativeSwitch.setChecked(true); // 스위치를 선택한 모양으로 만듬
                } else {
                    relativeSwitch.setChecked(false); // 스위치를 선택하지 않은 모양으로 만듬
                }

                if (mealarm.equals("o")) { // 사용자가 용병모집 알람 여부를 켜놨을 때
                    mercenarySwitch.setChecked(true); // 스위치를 선택한 모양으로 만듬
                } else {
                    mercenarySwitch.setChecked(false); // 스위치를 선택하지 않은 모양으로 만듬
                }

                if (noticealarm.equals("o")) { // 사용자가 공지사항 알람 여부를 켜놨을 때
                    noticeSwitch.setChecked(true); // 스위치를 선택한 모양으로 만듬
                } else {
                    noticeSwitch.setChecked(false); // 스위치를 선택하지 않은 모양으로 만듬
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
