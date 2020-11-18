package com.example.capstone_project.alarm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_project.R;
import com.example.capstone_project.User;
import com.example.capstone_project.mercenary.MercenaryBoardActivity;
import com.example.capstone_project.place.PlaceActivity;
import com.example.capstone_project.relative.RelativeBoardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    private TextView place_textView, date_textView; // 텍스트 뷰들 선언
    private Button place_btn, confirm_btn, date_btn; // 버튼들 선언
    private String date ="", choicePlace ="", name, uid, loginWay, userToken, number, replace ="", redate ="",
            meplace ="", medate ="", realarm, mealarm, noticealarm, current_uid; // 쓰이는 문자열 선언
    private DatePickerDialog dialog; // 매칭날짜 다이어로그
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference user_database, user_database2; // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private User user; // User 아이템 객체 선언

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        dialog = new DatePickerDialog(AlarmActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        // 현재 월/일 이전은 선택 불가하게 설정

        // 지역설정 버튼을 눌렀을 때 동작
        place_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaceActivity.class);
                startActivityForResult(intent, 0);
                // 지역설정 화면으로 이동
            }
        });

        // 일자설정 버튼을 눌렀을 때 동작
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                // 일자설정 다이어로그 띄우기
            }
        });

        // 확인 버튼을 눌렀을 때 동작
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmData();
                // alarmData 메소드 호출
                if (number.equals("1")) {
                    Intent intent = new Intent(getApplicationContext(), RelativeBoardActivity.class);
                    startActivity(intent);
                    // RelativeBoardActivity로 이동
                } else if (number.equals("2")) {
                    Intent intent = new Intent(getApplicationContext(), MercenaryBoardActivity.class);
                    startActivity(intent);
                    // MercenaryBoardActivity로 이동
                }
            }
        });
    }

    private void init() {
        place_textView = findViewById(R.id.place_textView);
        date_textView = findViewById(R.id.date_textView);
        place_btn = findViewById(R.id.place_btn);
        date_btn = findViewById(R.id.date_btn);
        confirm_btn = findViewById(R.id.confirm_btn);

        dialog = new DatePickerDialog(AlarmActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        // 현재 월/일 이전은 선택 불가하게 설정

        Intent intent = getIntent();
        number = intent.getStringExtra("number");
        // 버튼을 누른 게시판을 구별함(1이면 상대매칭, 2이면 용병모집)
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year); // 선택한 년으로 설정
            myCalendar.set(Calendar.MONTH, month); // 선택한 달로 설정
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // 선택한 일로 설정

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            date = sdf.format(myCalendar.getTime());
            date_textView.setText(date);
            // 매칭 날자를 출력형식에 맞춘 후 변수에 넣은 후, 텍스트뷰에 출력
        }
    };

    // 사용자가 매칭지역을 선택하고 돌아왔을 때 동작
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) { // 사용자가 매칭지역을 설정하고 돌아왔을 때
            if (resultCode == RESULT_OK) {
                choicePlace = data.getStringExtra("region");
                place_textView.setText(choicePlace);
                // 사용자가 선택한 지역을 변수에 넣은 후, 텍스트뷰에 출력
            }
        }
    }

    // 확인 눌렀을 때 호출되는 메소드
    private void alarmData() {
        if (choicePlace.isEmpty() || date.isEmpty()) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
            // 사용자가 빈칸없이 선택하지 않았을 때 Toast 메세지 전송
        } else {
            firebaseDatabase = FirebaseDatabase.getInstance();
            // 파이어베이스 데이터베이스 객체 생성
            auth = FirebaseAuth.getInstance();
            // 파이어베이스 인증 객체 생성
            FirebaseUser currentUser = auth.getCurrentUser();
            // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
            current_uid = currentUser.getUid();
            // 현재 접속한 사용자의 uid을 가져옴

            user_database = firebaseDatabase.getReference("users");
            // 알림 데이터를 설정하기 위한 파이어베이스 경로 설정
            Query query = user_database.orderByChild("uid").equalTo(current_uid);
            // users 키에 있는 사용자들의 uid와 현재 사용자의 uid를 비교

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출출
                        User userItem = snapshot.getValue(User.class);
                        // User 객체에 데이터를 담음
                        name = userItem.getName();
                        uid = userItem.getUid();
                        loginWay = userItem.getLoginWay();
                        userToken = userItem.getUserToken();
                        replace = userItem.getReplace();
                        redate = userItem.getRedate();
                        meplace = userItem.getMeplace();
                        medate = userItem.getMedate();
                        realarm = userItem.getRealarm();
                        mealarm = userItem.getMealarm();
                        noticealarm = userItem.getNoticealarm();
                        // 데이터베이스에서 일치하는 사용자의 정보들을 가져옴
                    }

                   user_database2 = user_database.child(uid);
                    // 현재 사용자의 users 키에 접근하기 위한 파이어베이스 경로 설정

                    if (number.equals("1")) {
                        user = new User(name, uid, loginWay, userToken, realarm, mealarm, choicePlace, date, meplace, medate, noticealarm);
                        // 상대매칭 게시판의 알림 설정 일때
                    } else if (number.equals("2")) {
                        user = new User(name, uid, loginWay, userToken, realarm, mealarm, replace, redate, choicePlace, date, noticealarm);
                        // 용병모집 게시판의 알림 설정 일때
                    }
                    user_database2.setValue(user);
                    // 해당 유저의 알림조건 설정 파이어베이스 업로드 구문
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(getApplicationContext(), "알림조건이 설정되었습니다.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송
        }
    }
}
