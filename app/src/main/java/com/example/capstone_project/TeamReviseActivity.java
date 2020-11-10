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

public class TeamReviseActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog;
    private Button place_btn, date_btn, revise_btn;
    private TextView date_textView, place_textView;
    private EditText title_edit, person_edit, content_edit, name_edit;
    private Spinner ability_spinner;
    private String[] spinnerAbility;
    private String title, day, name, person, content, ability, key, boardnumber, place;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;    // 파이버에시스 연결(경로) 선언
    private int spinnerNum, abilityNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_writing);

        init();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("team");

        Query query = databaseReference.orderByChild("boardnumber").equalTo(boardnumber);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    TeamBoardItem teamBoardItem = snapshot.getValue(TeamBoardItem.class);

                    place = teamBoardItem.getPlace();
                    day = teamBoardItem.getDay();
                    title = teamBoardItem.getTitle();
                    name = teamBoardItem.getName();
                    content = teamBoardItem.getContent();
                    ability = teamBoardItem.getAbility();
                    person = teamBoardItem.getPerson();
                }

                if (ability.equals("상")) {
                    abilityNum = 1;
                } else if (ability.equals("중")) {
                    abilityNum = 2;
                } else if (ability.equals("하")) {
                    abilityNum = 3;
                }

                    place_textView.setText(place);
                    date_textView.setText(day.substring(5));
                    ability_spinner.setSelection(abilityNum);
                    name_edit.setText(name);
                    title_edit.setText(title);
                    content_edit.setText(content);
                    person_edit.setText(person);
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
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
        name_edit = findViewById(R.id.name);

        spinnerAbility = getResources().getStringArray(R.array.ability);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(spinnerAbility, this);
        ability_spinner.setAdapter(spinnerAdapter);

        dialog = new DatePickerDialog(TeamReviseActivity.this, listener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
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

            String myFormat = "yyyy/MM/dd"; // 출력형식
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            day = sdf.format(myCalendar.getTime()); // 현재 날짜를 변수에 넣기

            date_textView.setText(day);
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

        if (title.isEmpty() || content.isEmpty() || day.isEmpty() || (name.isEmpty())
                || (ability.equals("실력")) || person.isEmpty() || (place.isEmpty())) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference2 = firebaseDatabase.getReference("board").child("team").child(key);

            Map<String, Object> boardChange = new HashMap<>();
            boardChange.put("place", place);
            boardChange.put("day", day);
            boardChange.put("name", name);
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

