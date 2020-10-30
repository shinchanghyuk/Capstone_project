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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MercenaryReviseActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog;
    private Button place_btn, date_btn, revise_btn;
    private TextView date_textView, place_textView;
    private EditText title_edit, person_edit, content_edit;
    private Spinner startTime, endTime, ability_spinner;
    private String[] spinnerTime, spinnerAbility;
    private String title, day, sTime, eTime, person, content, ability, key, boardnumber, place, type;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;    // 파이버에시스 연결(경로) 선언
    private int spinnerNum, abilityNum, sTimeNum, eTimeNum;
    private RadioGroup type_radio;
    private RadioButton radio1, radio2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mercenary_writing);

        init();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("mercenary");

        Query query = databaseReference.orderByChild("boardnumber").equalTo(boardnumber);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    MercenaryBoardItem mercenaryBoardItem = snapshot.getValue(MercenaryBoardItem.class);

                    place = mercenaryBoardItem.getPlace();
                    day = mercenaryBoardItem.getDay();
                    title = mercenaryBoardItem.getTitle();
                    type = mercenaryBoardItem.getType();
                    content = mercenaryBoardItem.getContent();
                    ability = mercenaryBoardItem.getAbility();
                    sTime = mercenaryBoardItem.getStarttime();
                    eTime = mercenaryBoardItem.getEndtime();
                    person = mercenaryBoardItem.getPerson();
                }

                if (ability.equals("상")) {
                    abilityNum = 1;
                } else if (ability.equals("중")) {
                    abilityNum = 2;
                } else if (ability.equals("하")) {
                    abilityNum = 3;
                }

                 for (int i = 0; i < spinnerTime.length; i++) {
                    if (spinnerTime[i].equals(sTime)) {
                        sTimeNum = i;
                    }
                    if (spinnerTime[i].equals(eTime)) {
                        eTimeNum = i;
                    }
                }

                 if(type.equals("용병")) {
                     radio1.setChecked(true);
                     radio2.setChecked(false);
                 } else if(type.equals("팀")) {
                     radio1.setChecked(false);
                     radio2.setChecked(true);
                 }

                    place_textView.setText(place);
                    date_textView.setText(day);
                    ability_spinner.setSelection(abilityNum);
                    startTime.setSelection(sTimeNum);
                    endTime.setSelection(eTimeNum);
                    title_edit.setText(title);
                    content_edit.setText(content);
                    person_edit.setText(person);
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
                }
        });

        type_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio1) {
                    type = "용병";
                } else if (checkedId == R.id.radio2) {
                    type = "팀";
                }
            }
        });

        startTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTime = (String) parent.getItemAtPosition(position);
                    spinnerNum = position;
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        endTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eTime = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ability_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ability = (String) parent.getItemAtPosition(position);
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

        revise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateChange();
            }
        });
    }

    private void init() {
        date_btn = findViewById(R.id.date_btn);
        place_btn = findViewById(R.id.place_btn);
        revise_btn = findViewById(R.id.btnUp);
        date_textView = findViewById(R.id.date_textView);
        place_textView = findViewById(R.id.place_textView);
        title_edit = findViewById(R.id.title_edit);
        person_edit = findViewById(R.id.person);
        ability_spinner = findViewById(R.id.ability);
        content_edit = findViewById(R.id.content_edit);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        type_radio = findViewById(R.id.type_radio);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);

        spinnerTime = getResources().getStringArray(R.array.time);
        spinnerAbility = getResources().getStringArray(R.array.ability);

        SpinnerAdapter spinnerAdapter1 = new SpinnerAdapter(spinnerTime, this);
        SpinnerAdapter spinnerAdapter3 = new SpinnerAdapter(spinnerAbility, this);

        startTime.setAdapter(spinnerAdapter1);
        endTime.setAdapter(spinnerAdapter1);
        ability_spinner.setAdapter(spinnerAdapter3);

        dialog = new DatePickerDialog(MercenaryReviseActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis()); // 현재 월/일 이전은 선택 불가하게 설정

        Intent intent = getIntent();
        boardnumber = intent.getStringExtra("bordernumber"); // 해당 게시글의 번호
        key = intent.getStringExtra("key"); // 해당 게시글의 키
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year); // 선택한 년으로 설정
            myCalendar.set(Calendar.MONTH, month); // 선택한 달로 설정
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // 선택한 일로 설정

            String myFormat = "MM/dd"; // 출력형식
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            day = sdf.format(myCalendar.getTime()); // 현재 날짜를 변수에 넣기

            date_textView.setText(sdf.format(myCalendar.getTime()));
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                place = data.getStringExtra("region");
                place_textView.setText(place);
            }
        } else if (requestCode == 1) {
        }
    }
    private void dateChange() {
        title = title_edit.getText().toString();
        person = person_edit.getText().toString();
        content = content_edit.getText().toString();

        if (title.isEmpty() || content.isEmpty() || day.isEmpty() || (sTime.equals("시간선택"))
                || (eTime.equals("시간선택")) || (ability.equals("실력")) || person.isEmpty() || (place.isEmpty())) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference2 = firebaseDatabase.getReference("board").child("mercenary").child(key);

            Map<String, Object> boardChange = new HashMap<>();
            boardChange.put("place", place);
            boardChange.put("day", day);
            boardChange.put("starttime", sTime);
            boardChange.put("endtime", eTime);
            boardChange.put("type", type);
            boardChange.put("person", person);
            boardChange.put("title", title);
            boardChange.put("content", content);
            boardChange.put("ability", ability);

            databaseReference2.updateChildren(boardChange);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}

