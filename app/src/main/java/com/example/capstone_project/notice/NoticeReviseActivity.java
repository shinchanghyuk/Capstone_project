package com.example.capstone_project.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_project.R;
import com.example.capstone_project.mypage.Manager;
import com.example.capstone_project.relative.RelativeBoardItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NoticeReviseActivity extends AppCompatActivity {

    private Button revise_btn; // 버튼들 선언
    private EditText title_edit, content_edit; // 사용자가 게시물을 수정 시 필요한 제목, 내용 edit 선언
    private String title, writetime, content, boardnumber; // 쓰이는 문자열 선언
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference notice_database, notice_database2; // 파이버에시스 연결(경로) 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_writing);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

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
        revise_btn = findViewById(R.id.btnUp);
        title_edit = findViewById(R.id.title_edit);
        content_edit = findViewById(R.id.content_edit);

        Intent intent = getIntent();
        boardnumber = intent.getStringExtra("boardnumber");
        // 해당 게시글의 번호를 가져와 boardnumber 변수에 값을 넣음

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        notice_database = firebaseDatabase.getReference("board").child("notice");
        // notice 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = notice_database.orderByChild("boardnumber").equalTo(boardnumber);
        // notice 키에 있는 boardnumber와 현재 사용자의 boardnumber를 비교
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    Manager noticeBoardItem = snapshot.getValue(Manager.class);

                    writetime = noticeBoardItem.getWritetime();
                    title = noticeBoardItem.getTitle();
                    content = noticeBoardItem.getContent();
                    // 데이터베이스에서 일치하는 게시물의 정보들을 가져옴
                }

                title_edit.setText(title);
                content_edit.setText(content);
                // 가져온 정보들을 텍스트 뷰에 보여줌
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void dataChange()  {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate1 = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
        writetime = simpleDate1.format(mDate);
        // 현재 시간을 출력형식에 맞춘 후 변수에 넣음

        title = title_edit.getText().toString();
        content = content_edit.getText().toString();
        // 사용자가 입력한 제목, 내용들을 변수에 넣음

        if (title.isEmpty() || content.isEmpty() || writetime.isEmpty()) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
            // 사용자가 빈칸없이 선택하지 않았을 때 Toast 메세지 전송
        } else {
            notice_database2 = notice_database.child(boardnumber);
            // 해당 게시물을 수정하기 위한 파이어베이스 경로 설정

            Map<String, Object> boardChange = new HashMap<>();
            boardChange.put("writetime", writetime);
            boardChange.put("title", title);
            boardChange.put("content", content);
            // 게시물 형식이 담긴 변수가 Map 형식으로 담기게 됨

            notice_database2.updateChildren(boardChange);
            // 해당 경로에 해당하는 게시물을 수정

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            // NoticeBoardContentActivity로 돌아가게 됨
            finish();
        }
    }
}

