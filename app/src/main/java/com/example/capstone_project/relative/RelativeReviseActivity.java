package com.example.capstone_project.relative;

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

import com.example.capstone_project.R;
import com.example.capstone_project.SpinnerAdapter;
import com.example.capstone_project.place.PlaceActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RelativeReviseActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog dialog; // 매칭날짜 다이어로그
    private Button place_btn, date_btn, revise_btn; // 버튼들 선언
    private TextView date_textView, place_textView; // 매칭지역, 매칭날짜를 나타내는 텍스트 뷰들 선언
    private EditText title_edit, person_edit, content_edit; // 사용자가 게시물을 수정 시 필요한 제목, 인원수, 내용 edit 선언
    private Spinner startTime, endTime, ability_spinner; // 시작 시간, 끝 시간, 실력 스피너 선언
    private String[] spinnerTime, spinnerAbility; // 스피너에 들어갈 목록인 문자열 배열 선언
    private String title, day, sTime, eTime, person, content, ability, boardnumber, place, writeTime; // 쓰이는 문자열 선언
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference relative_database; // 파이버에시스 연결(경로) 선언
    private int spinnerNum1, spinnerNum2, abilityNum, sTimeNum, eTimeNum; // 이전에 사용자가 작성한 값을 불러오기 위한 int 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_writing);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        dialog = new DatePickerDialog(RelativeReviseActivity.this, listener, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        // 현재 월/일 이전은 선택 불가하게 설정

        // 시작시간 스피너를 눌렀을 때 동작
        startTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTime = (String) parent.getItemAtPosition(position); // 사용자가 변경해서 선택한 목록을 가져와 변수에 넣음
                spinnerNum1 = position; // 사용자가 변경해서 선택한 목록의 위치를 가져와 변수에 넣음
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 끝시간 스피너를 눌렀을 때 동작
        endTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eTime = (String) parent.getItemAtPosition(position); // 사용자가 변경해서 선택한 목록을 가져와 변수에 값을 넣음
                spinnerNum2 = position; // 사용자가 변경해서 선택한 목록의 위치를 가져와 변수에 값을 넣음
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 능력 스피너를 눌렀을 때 동작
        ability_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ability = (String) parent.getItemAtPosition(position);
                // 사용자가 변경해서 선택한 목록을 가져와 변수에 값을 넣음
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
                // 지역 설정화면으로 이동
            }
        });

        // 매칭날짜 설정 버튼을 눌렀을 때 동작
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                // 날짜를 선택할 수 있는 다이얼로그를 띄움
            }
        });

        // 확인 버튼을 눌렀을 때 동작
        revise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataChange();
                // dataChange 메소드 호출
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

        Intent intent = getIntent();
        boardnumber = intent.getStringExtra("boardnumber");
        // 해당 게시글의 번호를 가져와 boardnumber 변수에 값을 넣음

        spinnerTime = getResources().getStringArray(R.array.time);
        spinnerAbility = getResources().getStringArray(R.array.ability);
        // array.xml에 있는 데이터들을 가져와 문자열 배열 변수에 값을 넣음

        SpinnerAdapter timeAdapter = new SpinnerAdapter(spinnerTime, this);
        SpinnerAdapter abilityAdapter = new SpinnerAdapter(spinnerAbility, this);
        startTime.setAdapter(timeAdapter);
        endTime.setAdapter(timeAdapter);
        ability_spinner.setAdapter(abilityAdapter);
        // spinnerAdapter 객체를 생성해 시작시간, 끝시간, 실력 스피너에 담음

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        relative_database = firebaseDatabase.getReference("board").child("relative");
        // relative 키에 접근하기 위한 파이어베이스 경로 설정

        Query query = relative_database.orderByChild("boardnumber").equalTo(boardnumber);
        // relative 키에 있는 boardnumber와 현재 사용자의 boardnumber를 비교

        query.addListenerForSingleValueEvent(new ValueEventListener() { // relative 키에 일치하는 boardnumber가 있다면
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                    RelativeBoardItem relativeBoardItem = snapshot.getValue(RelativeBoardItem.class);

                    place = relativeBoardItem.getPlace();
                    day = relativeBoardItem.getDay();
                    title = relativeBoardItem.getTitle();
                    content = relativeBoardItem.getContent();
                    ability = relativeBoardItem.getAbility();
                    sTime = relativeBoardItem.getStarttime();
                    eTime = relativeBoardItem.getEndtime();
                    person = relativeBoardItem.getPerson();
                    // 데이터베이스에서 일치하는 게시물의 정보들을 가져옴
                }

                if (ability.equals("상")) { // 가져온 데이터가 상일 때
                    abilityNum = 1;
                } else if (ability.equals("중")) { // 가져온 데이터가 중일 때
                    abilityNum = 2;
                } else if (ability.equals("하")) {// 가져온 데이터가 하일 때
                    abilityNum = 3;
                }

                for (int i = 0; i < spinnerTime.length; i++) {
                    if (spinnerTime[i].equals(sTime)) {
                        sTimeNum = i;
                    }
                    if (spinnerTime[i].equals(eTime)) {
                        eTimeNum = i;
                    }
                }  // 가져온 데이터를 통해 사용자가 선택한 데이터가 있는 위치를 변수에 넣음

                place_textView.setText(place);
                date_textView.setText(day.substring(5));
                ability_spinner.setSelection(abilityNum);
                startTime.setSelection(sTimeNum);
                endTime.setSelection(eTimeNum);
                title_edit.setText(title);
                content_edit.setText(content);
                person_edit.setText(person);
                // 가져온 정보들을 텍스트 뷰 또는 스피너에 보여줌
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year); // 선택한 년으로 설정
            myCalendar.set(Calendar.MONTH, month); // 선택한 달로 설정
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // 선택한 일로 설정

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            day = sdf.format(myCalendar.getTime());
            date_textView.setText(day);
            // 매칭 날자를 출력형식에 맞춘 후 변수에 넣은 후, 텍스트뷰에 출력
        }
    };

    // 사용자가 매칭지역을 수정하고 돌아왔을 때 동작
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) { // 사용자가 다시 지역을 설정하고 돌아왔을 때
            if (resultCode == RESULT_OK) {
                place = data.getStringExtra("region");
                place_textView.setText(place);
                // 사용자가 선택한 지역을 변수에 넣은 후, 텍스트뷰에 출력
            }
        }
    }

    // 확인 눌렀을 때 호출되는 메소드
    private void dataChange() {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate1 = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
        writeTime = simpleDate1.format(mDate);
        // 현재 시간을 출력형식에 맞춘 후 변수에 넣음

        title = title_edit.getText().toString();
        person = person_edit.getText().toString();
        content = content_edit.getText().toString();
        // 사용자가 입력한 제목, 인원 수, 내용들을 변수에 넣음

        if (title.isEmpty() || content.isEmpty() || day.isEmpty() || (sTime.equals("시간선택"))
                || (eTime.equals("시간선택")) || (ability.equals("실력")) || person.isEmpty() || (place.isEmpty())) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
            // 사용자가 빈칸없이 선택하지 않았을 때 Toast 메세지 전송
        } else if (spinnerNum1 >= spinnerNum2) {
            Toast.makeText(getApplicationContext(), "잘못된 시간 설정입니다.", Toast.LENGTH_SHORT).show();
            // 시작시간이 끝시간과 같거나 클 때 Toast 메세지 전송
        } else {
            // 사용자가 모두 알맞게 입력 하였을 때
            relative_database = firebaseDatabase.getReference("board").child("relative").child(boardnumber);
            // 해당 게시물을 수정하기 위한 파이어베이스 경로 설정

            Map<String, Object> boardChange = new HashMap<>();
            boardChange.put("place", place);
            boardChange.put("day", day);
            boardChange.put("starttime", sTime);
            boardChange.put("endtime", eTime);
            boardChange.put("person", person);
            boardChange.put("title", title);
            boardChange.put("content", content);
            boardChange.put("ability", ability);
            boardChange.put("writetime", writeTime);
            // 게시물 형식이 담긴 변수가 Map 형식으로 담기게 됨

            relative_database.updateChildren(boardChange);
            // 해당 경로에 해당하는 게시물을 수정

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            // RelativeBoardContentActivity로 돌아가게 됨
            finish();
        }
    }
}

