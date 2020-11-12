package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AlarmActivity extends AppCompatActivity {
    private TextView place_textView, date_textView;
    private Button place_btn, confirm_btn, date_btn;
    private String date="", choicePlace="", currentUser, name, uid, loginWay, userToken, number, replace="", redate="",
        meplace="", medate="", realarm, mealarm, noticealarm;
    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;  // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        init();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        currentUser = user.getUid();

        place_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaceActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmData();
                if(number.equals("1")) {
                    Intent intent = new Intent(getApplicationContext(), RelativeBoardActivity.class);
                    startActivity(intent);
                } else if(number.equals("2")) {
                    Intent intent = new Intent(getApplicationContext(), MercenaryBoardActivity.class);
                    startActivity(intent);
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
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis()); // 현재 월/일 이전은 선택 불가하게 설정

        Intent intent = getIntent();
        number = intent.getStringExtra("number"); // 누른 게시글의 번호
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year); // 선택한 년으로 설정
            myCalendar.set(Calendar.MONTH, month); // 선택한 달로 설정
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // 선택한 일로 설정

            String myFormat = "yyyy/MM/dd"; // 출력형식
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            date = sdf.format(myCalendar.getTime()); // 현재 날짜를 변수에 넣기
            date_textView.setText(date);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                choicePlace = data.getStringExtra("region");
                place_textView.setText(choicePlace);
            }
        } else if (requestCode == 1) {
        }
    }

    private void alarmData() {
        if (choicePlace.isEmpty() || date.isEmpty()) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("users");

            Query query = databaseReference.orderByChild("uid").equalTo(currentUser);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                        User userItem = snapshot.getValue(User.class);
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
                    }

                    databaseReference2 = databaseReference.child(uid);

                    if (number.equals("1")) {
                        user = new User(name, uid, loginWay, userToken, realarm, mealarm, choicePlace, date, meplace, medate , noticealarm);

                    } else if (number.equals("2")) {
                        user = new User(name, uid, loginWay, userToken, realarm, mealarm, replace, redate, choicePlace, date, noticealarm);

                    }
                    databaseReference2.setValue(user);
                }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            Toast.makeText(getApplicationContext(), "알림조건이 설정되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
