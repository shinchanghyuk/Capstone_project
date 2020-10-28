package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TeamWritingActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog;
    private Button place_btn, confirm_btn, date_btn;
    private TextView date_textView,place_textView;
    private EditText title_edit, person, content_edit, name;
    private Spinner ability;
    private String total_matching, total_title = "", total_day ="", total_user, total_name="", choicePlace="";
    private String total_ability, total_person="", total_content="", boardNumber;
    private String[] spinnerAbility;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference;    // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private int spinnerNum; // 파이어베이스 안에 있는 데이터 갯수(게시판 갯수)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_writing);

        init();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("team").push();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        total_user = user.getDisplayName();

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
        date_textView = findViewById(R.id.date_textView);
        confirm_btn = findViewById(R.id.btnUp);
        place_textView = findViewById(R.id.place_textView);
        title_edit = findViewById(R.id.title_edit);
        person = findViewById(R.id.person);
        ability = findViewById(R.id.ability);
        content_edit = findViewById(R.id.content_edit);
        name = findViewById(R.id.name);

        spinnerAbility = getResources().getStringArray(R.array.ability);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(spinnerAbility, this);
        ability.setAdapter(spinnerAdapter);
        dialog = new DatePickerDialog(TeamWritingActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis()); // 현재 월/일 이전은 선택 불가하게 설정

        Log.d("date", String.valueOf(myCalendar.get(Calendar.YEAR)));
        Log.d("date1", String.valueOf(myCalendar.get(Calendar.MONTH)));
        Log.d("date2", String.valueOf(myCalendar.get(Calendar.DAY_OF_MONTH)));
    }
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year); // 선택한 년으로 설정
            myCalendar.set(Calendar.MONTH, month); // 선택한 달로 설정
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // 선택한 일로 설정

            String myFormat = "MM/dd"; // 출력형식
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            total_day = "~" + sdf.format(myCalendar.getTime()); // 현재 날짜를 변수에 넣기
            date_textView.setText(sdf.format(myCalendar.getTime()));
        }
    };
    private void userdata() {
        total_matching = "모집 중";
        total_title = title_edit.getText().toString();
        total_person = person.getText().toString();
        total_content = content_edit.getText().toString();
        total_name = name.getText().toString();
        boardNumber = databaseReference.push().getKey();

        if (total_title.isEmpty() || total_content.isEmpty() || total_day.isEmpty() || (total_name.equals("팀 이름"))
                    || (total_ability.equals("실력")) || total_person.isEmpty() || (choicePlace.isEmpty())) {
                Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            else {
          TeamBoardItem teamBoardItem = new TeamBoardItem(total_matching, total_title, total_day, total_user,
                    choicePlace, total_name, boardNumber, total_ability, total_person, total_content);

                databaseReference.setValue(teamBoardItem);
                Toast.makeText(getApplicationContext(), "게시물이 작성 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
        }
    }

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