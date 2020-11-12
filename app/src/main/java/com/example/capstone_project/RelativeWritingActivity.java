package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_project.PlaceActivity;
import com.example.capstone_project.R;
import com.example.capstone_project.RelativeBoardItem;
import com.example.capstone_project.SpinnerAdapter;
import com.example.capstone_project.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RelativeWritingActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog;
    private Button place_btn, date_btn, confirm_btn;
    private TextView date_textView, place_textView;
    private EditText title_edit, person, content_edit;
    private Spinner startTime, endTime, ability;
    private String total_matching, total_title, total_day="", total_user, total_sTime, choicePlace="";
    private String total_eTime, total_ability, total_person , total_content, boardNumber, total_uid;
    private String alarm_content, alarm_title, fcmUrl, serverKey, fcmToken;
    private String[] spinnerTime1, spinnerTime2, spinnerAbility;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;    // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private int spinnerNum1, spinnerNum2; // 파이어베이스 안에 있는 데이터 갯수(게시판 갯수)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_writing);

        init();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("relative").push();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        total_user = user.getDisplayName();
        total_uid = user.getUid();

        startTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                total_sTime = (String) parent.getItemAtPosition(position);
                spinnerNum1 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        endTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                total_eTime = (String) parent.getItemAtPosition(position);
                spinnerNum2 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                total_ability = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                userdata();
            }
        });
    }

    private void init() {
        date_btn = findViewById(R.id.date_btn);
        place_btn = findViewById(R.id.place_btn);
        confirm_btn = findViewById(R.id.btnUp);
        date_textView = findViewById(R.id.date_textView);
        place_textView = findViewById(R.id.place_textView);
        title_edit = findViewById(R.id.title_edit);
        person = findViewById(R.id.person);
        ability = findViewById(R.id.ability);
        content_edit = findViewById(R.id.content_edit);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        spinnerTime1 = getResources().getStringArray(R.array.time);
        spinnerAbility = getResources().getStringArray(R.array.ability);

        SpinnerAdapter spinnerAdapter1 = new SpinnerAdapter(spinnerTime1, this);
        SpinnerAdapter spinnerAdapter3 = new SpinnerAdapter(spinnerAbility, this);
        startTime.setAdapter(spinnerAdapter1);
        endTime.setAdapter(spinnerAdapter1);
        ability.setAdapter(spinnerAdapter3);

        dialog = new DatePickerDialog(RelativeWritingActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis()); // 현재 월/일 이전은 선택 불가하게 설정

        fcmUrl = "https://fcm.googleapis.com/fcm/send";
        serverKey = getResources().getString(R.string.server_key);
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year); // 선택한 년으로 설정
            myCalendar.set(Calendar.MONTH, month); // 선택한 달로 설정
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // 선택한 일로 설정

            String myFormat = "yyyy/MM/dd"; // 출력형식
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            total_day = sdf.format(myCalendar.getTime()); // 현재 날짜를 변수에 넣기

            date_textView.setText(total_day); //월/일을 잘라서 출력
        }
    };

    private void userdata() {
        total_matching = "매칭 중";
        total_title = title_edit.getText().toString();
        total_person = person.getText().toString();
        total_content = content_edit.getText().toString();
        boardNumber = databaseReference.getKey();

        if (total_title.isEmpty() || total_content.isEmpty() || total_day.isEmpty() || (total_sTime.equals("시간선택"))
                || (total_eTime.equals("시간선택")) || (total_ability.equals("실력")) || total_person.isEmpty() || (choicePlace.isEmpty())) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (spinnerNum1 >= spinnerNum2) {
            Toast.makeText(getApplicationContext(), "잘못된 시간 설정입니다.", Toast.LENGTH_SHORT).show();
        } else {
            RelativeBoardItem relativeBoardItem = new RelativeBoardItem(total_matching, total_title, total_day, total_user,
                    choicePlace, total_sTime, boardNumber, total_eTime, total_ability, total_person, total_content, total_uid);

            databaseReference.setValue(relativeBoardItem);
            Toast.makeText(getApplicationContext(), "게시물이 작성 되었습니다.", Toast.LENGTH_SHORT).show();

            databaseReference2 = firebaseDatabase.getReference("users");

            alarm_title = "상대매칭 게시판 알림";
            alarm_content = "입력하신 조건과 부합하는 게시글이 등록되었습니다.";

            Query query = databaseReference2.orderByChild("replace").equalTo(choicePlace);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User userItem = snapshot.getValue(User.class);
                        if (userItem.getRedate().equalsIgnoreCase(total_day)) {
                            fcmToken = userItem.getUserToken();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        JSONObject root = new JSONObject();
                                        JSONObject notification = new JSONObject();
                                        notification.put("body", alarm_content);
                                        notification.put("title", alarm_title);
                                        root.put("notification", notification);
                                        root.put("to", fcmToken);
                                        URL Url = new URL(fcmUrl);
                                        HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                        // URL 연결
                                        conn.setRequestMethod("POST");
                                        conn.setDoOutput(true);
                                        conn.setDoInput(true);
                                        conn.addRequestProperty("Authorization", "key=" + serverKey);
                                        conn.setRequestProperty("Accept", "application/json");
                                        conn.setRequestProperty("Content-type", "application/json");
                                        OutputStream os = conn.getOutputStream();
                                        os.write(root.toString().getBytes("utf-8"));
                                        os.flush();
                                        conn.getResponseCode();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            finish();
        }
    }

    /*   private void spinnerChange(int length, int position) {
           spinnerTime2 = new String[length - position];

           for(int i = spinnerNum, j = 0; i < spinnerTime1.length; i++, j++) {
               Log.d("spinn", String.valueOf(spinnerTime1.length));
               spinnerTime2[j] = spinnerTime1[i];
               Log.d("spinn", String.valueOf(spinnerTime2[i]));
           }
           SpinnerAdapter spinnerAdapter2 = new SpinnerAdapter(spinnerTime2, this);
           endTime.setAdapter(spinnerAdapter2);
       } */
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
}