package com.example.capstone_project.relative;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.capstone_project.R;
import com.example.capstone_project.SpinnerAdapter;
import com.example.capstone_project.User;
import com.example.capstone_project.place.PlaceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RelativeWritingActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog; // 매칭날짜 다이어로그
    private Button place_btn, date_btn, confirm_btn; // 버튼들 선언
    private TextView date_textView, place_textView; // 매칭지역, 매칭날짜를 나타내는 텍스트 뷰들 선언
    private EditText title_edit, person, content_edit; // 사용자가 게시물을 작성 시 필요한 제목, 인원수, 내용 edit 선언
    private Spinner startTime, endTime, ability; // 시작 시간, 끝 시간, 실력 스피너 선언
    private String total_matching, total_title, total_day = "", total_user, total_sTime, choicePlace = "",
            total_eTime, total_ability, total_person, total_content, boardnumber, total_uid,
            alarm_content, alarm_title, fcmUrl, serverKey, fcmToken, writeTime; // 쓰이는 문자열 선언
    private String[] spinnerTime, spinnerAbility; // 스피너에 들어갈 목록인 문자열 배열 선언
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference relative_database, user_database; // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private int spinnerNum1, spinnerNum2; // 사용자가 선택한 스피너의 목록 위치를 가져오기 위한 int 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_writing);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        dialog = new DatePickerDialog(RelativeWritingActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis()); // 현재 월/일 이전은 선택 불가하게 설정

        // 시작시간 스피너를 눌렀을 때 동작
        startTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                total_sTime = (String) parent.getItemAtPosition(position); // 사용자가 스피너에서 선택한 목록을 가져와 변수에 넣음
                spinnerNum1 = position; // 사용자가 선택한 목록의 위치를 가져와 변수에 넣음
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 끝시간 스피너를 눌렀을 때 동작
        endTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                total_eTime = (String) parent.getItemAtPosition(position); // 사용자가 스피너에서 선택한 목록을 가져와 변수에 넣음
                spinnerNum2 = position; // 사용자가 선택한 목록의 위치를 가져와 변수에 넣음
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 능력 스피너를 눌렀을 때 동작
        ability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                total_ability = (String) parent.getItemAtPosition(position);
                // 사용자가 스피너에서 선택한 목록을 가져와 변수에 넣음
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 매칭지역 설정 버튼을 눌렀을 때 동작
        place_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaceActivity.class);
                startActivityForResult(intent, 0);
                // 지역설정 화면으로 이동
            }
        });

        // 매칭날짜 설정 버튼을 눌렀을 때 동작
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                // 매칭날짜 다이어로그 띄우기
            }
        });

        // 확인 버튼을 눌렀을 때 동작
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdata();
                // userdata 메소드 호출
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

        spinnerTime = getResources().getStringArray(R.array.time);
        spinnerAbility = getResources().getStringArray(R.array.ability);
        // array.xml에 있는 데이터들을 가져와 문자열 배열 변수에 값을 넣음

        SpinnerAdapter timeAdapter = new SpinnerAdapter(spinnerTime, this);
        SpinnerAdapter abilityAdapter = new SpinnerAdapter(spinnerAbility, this);
        startTime.setAdapter(timeAdapter);
        endTime.setAdapter(timeAdapter);
        ability.setAdapter(abilityAdapter);
        // spinnerAdapter 객체를 생성해 시작시간, 끝시간, 실력 스피너에 담음

        fcmUrl = "https://fcm.googleapis.com/fcm/send";
        serverKey = getResources().getString(R.string.server_key);
        // 알림을 사용자에게 보내기 위한 url 및 서버키를 변수에 넣음
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year); // 선택한 년으로 설정
            myCalendar.set(Calendar.MONTH, month); // 선택한 달로 설정
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // 선택한 일로 설정

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            total_day = sdf.format(myCalendar.getTime());
            date_textView.setText(total_day);
            // 매칭 날자를 출력형식에 맞춘 후 변수에 넣은 후, 텍스트뷰에 출력
        }
    };

    // 확인 눌렀을 때 호출되는 메소드
    private void userdata() {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
        writeTime = sdf.format(mDate);
        // 현재 시간을 출력형식에 맞춘 후 변수에 넣음

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        relative_database = firebaseDatabase.getReference("board").child("relative").push();
        // 해당 게시물을 relative 키에 새로 넣기 위한 파이어베이스 경로 설정
        auth = FirebaseAuth.getInstance();
        // 파이어베이스 인증 객체 생성
        FirebaseUser user = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성

        total_user = user.getDisplayName();
        total_uid = user.getUid();
        // 현재 접속한 사용자의 uid와 이름을 가져옴
        total_matching = "매칭 중";
        // 게시물을 올리기 때문에 처음 부여되는 값은 매칭 중임
        total_title = title_edit.getText().toString();
        total_person = person.getText().toString();
        total_content = content_edit.getText().toString();
        // 사용자가 입력한 제목, 인원 수, 내용들을 변수에 넣음

        boardnumber = relative_database.getKey();
        // 이 게시물의 키를 boardnumber 변수에 넣음

        if (total_title.isEmpty() || total_content.isEmpty() || total_day.isEmpty() || (total_sTime.equals("시간선택"))
                || (total_eTime.equals("시간선택")) || (total_ability.equals("실력")) || total_person.isEmpty() || (choicePlace.isEmpty())) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요.", Toast.LENGTH_SHORT).show();
            // 사용자가 빈칸없이 선택하지 않았을 때 Toast 메세지 전송
        } else if (spinnerNum1 >= spinnerNum2) {
            Toast.makeText(getApplicationContext(), "잘못된 시간 설정입니다.", Toast.LENGTH_SHORT).show();
            // 시작시간이 끝시간과 같거나 클 때 Toast 메세지 전송
        } else {
            // 사용자가 모두 알맞게 입력 하였을 때
            RelativeBoardItem relativeBoardItem = new RelativeBoardItem(total_matching, total_title, total_day, total_user,
                    choicePlace, total_sTime, boardnumber, total_eTime, total_ability, total_person, total_content, total_uid, writeTime);
            relative_database.setValue(relativeBoardItem);
            // 해당 게시글 파이어베이스 업로드 구문

            Toast.makeText(getApplicationContext(), "게시물이 작성 되었습니다.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송

            alarm_title = "상대매칭 게시판 알림";
            alarm_content = "입력하신 조건과 부합하는 게시글이 등록되었습니다.";
            // 받고자 하는 매칭지역 및 매칭날짜가 일치하는 사용자에게 보낼 알림 제목 및 내용을 변수에 넣음

            user_database = firebaseDatabase.getReference("users");
            // 상대매칭 알림을 받겠다고 한 유저들에게 알림을 보내기 위한 파이어베이스 경로 설정
            Query query = user_database.orderByChild("replace").equalTo(choicePlace);
            // users 키에 있는 사용자들의 replace와 게시물 작성자가 설정한 place를 비교

            query.addListenerForSingleValueEvent(new ValueEventListener() { // 알림 조건이 일치하는 사용자들에게 알림을 전송
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User userItem = snapshot.getValue(User.class);
                        if (userItem.getRedate().equalsIgnoreCase(total_day)) { // 매칭날짜도 비교함
                            fcmToken = userItem.getUserToken(); // 해당되는 사용자들의 알림 토큰을 가져와 변수에 넣음
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
                                        HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL 연결
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
                    } // 해당되는 사용자들에게 알림을 보내는 구문
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            finish();
        }
    }

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
}