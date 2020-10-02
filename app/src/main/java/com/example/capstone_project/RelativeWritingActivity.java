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

public class RelativeWritingActivity extends AppCompatActivity {

    private ArrayList<String> choicePlace;
    private RecyclerView place_recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PlaceItem placeItem;
    private ArrayList<PlaceItem> place_arrayList;
    private RecyclerView.Adapter place_adapter;
    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog;
    private Button place_btn, date_btn, btnUp;
    private TextView date_textView;
    private EditText title_edit, person, content_edit;
    private Spinner startTime, endTime, ability;
    private String total_matching, total_title = null, total_day, total_user, total_place, total_sTime;
    private String total_eTime, total_ability, total_person, total_content, total_uid;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference;    // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private Board board;
    private int boardNum, spinnerNum; // 파이어베이스 안에 있는 데이터 갯수(게시판 갯수)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_writing);

        init();

        Intent intent = getIntent();
        boardNum = intent.getIntExtra("boardNum", 0);
        Log.d("num1", String.valueOf(boardNum));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("relative").child("board" + boardNum);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        total_user = user.getDisplayName();

        startTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                total_sTime = (String) parent.getItemAtPosition(position);

                spinnerNum = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        endTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    total_eTime = (String) parent.getItemAtPosition(position);
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

        btnUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userdata();
        }
    });
}
    private void init() {
        date_btn = findViewById(R.id.date_btn);
        place_btn = findViewById(R.id.place_btn);
        btnUp = findViewById(R.id.btnUp);
        date_textView = findViewById(R.id.date_textView);
        title_edit = findViewById(R.id.title_edit);
        person = findViewById(R.id.person);
        ability = findViewById(R.id.ability);
        content_edit = findViewById(R.id.content_edit);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        date_textView = findViewById(R.id.date_textView);
        place_recyclerView = findViewById(R.id.place_recyclerView);

        place_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        place_recyclerView.setLayoutManager(layoutManager);

        place_arrayList = new ArrayList<>();
        choicePlace = new ArrayList<>();

        dialog = new DatePickerDialog(RelativeWritingActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
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

            Log.d("d", String.valueOf(year));
            Log.d("d1", String.valueOf(month));
            Log.d("d2", String.valueOf(dayOfMonth));

            String myFormat = "MM/dd"; // 출력형식
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            total_day = sdf.format(myCalendar.getTime()); // 현재 날짜를 변수에 넣기

            date_textView.setText(sdf.format(myCalendar.getTime()));
        }
    };
    private void item() {
        place_arrayList.clear();
        for (int i = 0; i < choicePlace.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(choicePlace.get(i));
            place_arrayList.add(placeItem);
    }
        place_adapter = new BoardAdapter(place_arrayList, this);
        place_recyclerView.setAdapter(place_adapter);
        place_adapter.notifyDataSetChanged();
    }
    private void userdata() {
        total_matching = "매칭 중";
        total_title = title_edit.getText().toString();
        total_person = person.getText().toString();
        total_content = content_edit.getText().toString();

        Log.d("content", total_content);
        Log.d("title", total_title);

        if (total_matching.isEmpty() || total_title.isEmpty() || total_content.isEmpty() || total_day.isEmpty()
                || total_user.isEmpty() || (total_sTime.equals("시간선택")) || (total_eTime.equals("시간선택")) || (total_ability.equals("실력")) ||
                total_person.isEmpty() || (choicePlace.isEmpty())) {

            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else {
            Board board = new Board(total_matching, total_title, total_day, total_user,
                    choicePlace, total_sTime, boardNum, total_eTime, total_ability, total_person, total_content);

            databaseReference.setValue(board);
            finish();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                choicePlace = data.getStringArrayListExtra("region");
                item();
            }
        } else if (requestCode == 1) {
        }
    }
}