package com.example.capstone_project.team;

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
import androidx.appcompat.app.AppCompatActivity;
import com.example.capstone_project.R;
import com.example.capstone_project.SpinnerAdapter;
import com.example.capstone_project.place.PlaceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TeamWritingActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog; // 매칭날짜 다이어로그
    private Button place_btn, date_btn, confirm_btn; // 버튼들 선언
    private TextView date_textView, place_textView; // 매칭지역, 매칭날짜를 나타내는 텍스트 뷰들 선언
    private EditText title_edit, person, content_edit, name; // 사용자가 게시물을 작성 시 필요한 제목, 인원수, 내용, 팀이름 edit 선언
    private Spinner ability; // 실력 스피너 선언
    private String total_matching, total_title, total_day = "", total_user, total_name, choicePlace = "",
            total_ability, total_person, total_content, boardNumber, total_uid, writeTime; // 쓰이는 문자열 선언
    private String[] spinnerAbility; // 스피너에 들어갈 목록인 문자열 배열 선언
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference team_database;    // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_writing);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        dialog = new DatePickerDialog(TeamWritingActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis()); // 현재 월/일 이전은 선택 불가하게 설정

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

        // 활동지역 설정 버튼을 눌렀을 때 동작
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
        date_textView = findViewById(R.id.date_textView);
        confirm_btn = findViewById(R.id.btnUp);
        place_textView = findViewById(R.id.place_textView);
        title_edit = findViewById(R.id.title_edit);
        person = findViewById(R.id.person);
        ability = findViewById(R.id.ability);
        content_edit = findViewById(R.id.content_edit);
        name = findViewById(R.id.name);

        spinnerAbility = getResources().getStringArray(R.array.ability);
        // array.xml에 있는 데이터들을 가져와 문자열 배열 변수에 값을 넣음

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(spinnerAbility, this);
        ability.setAdapter(spinnerAdapter);
        // spinnerAdapter 객체를 생성해 실력 스피너에 담음
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
        team_database = firebaseDatabase.getReference("board").child("team").push();
        // 해당 게시물을 team 키에 새로 넣기 위한 파이어베이스 경로 설정
        auth = FirebaseAuth.getInstance();
        // 파이어베이스 인증 객체 생성
        FirebaseUser user = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성

        total_user = user.getDisplayName();
        total_uid = user.getUid();
        // 현재 접속한 사용자의 uid와 이름을 가져옴
        total_matching = "모집 중";
        // 게시물을 올리기 때문에 처음 부여되는 값은 모집 중임
        total_title = title_edit.getText().toString();
        total_person = person.getText().toString();
        total_content = content_edit.getText().toString();
        total_name = name.getText().toString();
        // 사용자가 입력한 제목, 인원 수, 내용, 팀이름들을 변수에 넣음

        boardNumber = team_database.getKey();
        // 이 게시물의 키를 boardnumber 변수에 넣음

        if (total_title.isEmpty() || total_content.isEmpty() || total_day.isEmpty() || (total_name.equals("팀 이름"))
                || (total_ability.equals("실력")) || total_person.isEmpty() || (choicePlace.isEmpty())) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
            // 사용자가 빈칸없이 선택하지 않았을 때 Toast 메세지 전송
        } else {
            // 사용자가 모두 알맞게 입력 하였을 때
            TeamBoardItem teamBoardItem = new TeamBoardItem(total_matching, total_title, total_day, total_user,
                    choicePlace, total_name, boardNumber, total_ability, total_person, total_content, total_uid, writeTime);
            team_database.setValue(teamBoardItem);
            // 해당 게시글 파이어베이스 업로드 구문

            Toast.makeText(getApplicationContext(), "게시물이 작성 되었습니다.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송
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
