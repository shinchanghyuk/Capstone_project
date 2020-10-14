package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RelativeBoardContentActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;    // 파이버에시스 연결(경로) 선언
    private TextView matching_tv, place_tv, date_tv, person_tv, ability_tv, content_tv, title_tv, time_tv;
    private Button matching_btn, update_btn, delete_btn, list_btn, reply_btn;
    private String matching, day, title, content, ability, starttime, endtime, person, user, current_user, boardnumber, key, place;
    private FirebaseAuth auth; // 파이어베이스 인증 객체

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_board_content);

        init();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("relative");

        Log.d("number", boardnumber);

        Query query = databaseReference.orderByChild("boardnumber").equalTo(boardnumber);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    RelativeBoardContentItem relativeBoardItem = snapshot.getValue(RelativeBoardContentItem.class);
                    // RelativeBoardContentItem 객체에 데이터를 담음
                    key = snapshot.getKey();
                    matching = relativeBoardItem.getMatching();
                    place = relativeBoardItem.getPlace();
                    day = relativeBoardItem.getDay();
                    title = relativeBoardItem.getTitle();
                    content = relativeBoardItem.getContent();
                    ability = relativeBoardItem.getAbility();
                    starttime = relativeBoardItem.getStarttime();
                    endtime = relativeBoardItem.getEndtime();
                    person = relativeBoardItem.getPerson();
                    user = relativeBoardItem.getUser();
                }

                matching_tv.setText(matching);
                place_tv.setText(place);
                date_tv.setText(day);
                time_tv.setText(starttime + " ~ " + endtime);
                title_tv.setText(title);
                person_tv.setText(person);
                ability_tv.setText(ability);
                content_tv.setText(content);

                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                current_user = firebaseUser.getDisplayName();

                if(current_user.equals(user)) {
                matching_btn.setVisibility(View.VISIBLE);
                update_btn.setVisibility(View.VISIBLE);
                delete_btn.setVisibility(View.VISIBLE);
            }
        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        matching_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                if(matching.equals("매칭 중")) {
                    dialog.operation("matching1");
                } else if(matching.equals("매칭완료")) {
                    dialog.operation("matching2");
                }
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                dialog.operation("update");
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                dialog.operation("delete");
            }
        });

        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelativeBoardContentActivity.this, RelativeBoardActivity.class);
                startActivity(intent);
            }
        });

        reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 댓글 넣었을 시 동작
            }
        });
    }

    private void init() {
        matching_tv = findViewById(R.id.matching_tv);
        place_tv = findViewById(R.id.place_tv);
        date_tv = findViewById(R.id.date_tv);
        time_tv = findViewById(R.id.time_tv);
        person_tv = findViewById(R.id.person_tv);
        ability_tv = findViewById(R.id.ability_tv);
        title_tv = findViewById(R.id.title_tv);
        content_tv = findViewById(R.id.content_tv);
        matching_btn = findViewById(R.id.matching_btn);
        update_btn = findViewById(R.id.update_btn);
        delete_btn = findViewById(R.id.delete_btn);
        list_btn = findViewById(R.id.list_btn);
        reply_btn= findViewById(R.id.reply_btn);

        Intent intent = getIntent();
        boardnumber = intent.getStringExtra("boardnumber"); // 누른 게시글의 번호
    }
    public void matchingChange() {
        if(matching.equals("매칭 중")) { // DB의 matching 값이 매칭 중일때
            matching = "매칭완료";
            matching_btn.setText("매칭취소");
            Toast.makeText(getApplicationContext(), "매칭이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        else if(matching.equals("매칭완료")){ // DB의 matching 값이 매칭 완료일때
            matching = "매칭 중";
            matching_btn.setText("매칭완료");
            Toast.makeText(getApplicationContext(), "매칭이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
        }

        databaseReference2 = firebaseDatabase.getReference("board").child("relative").child(key);
        Map<String, Object> matchingOk = new HashMap<>();
        matchingOk.put("matching", matching);
        databaseReference2.updateChildren(matchingOk);
        matching_tv.setText(matching);
    }
    public void boardDelete() {
        databaseReference2 = firebaseDatabase.getReference("board").child("relative").child(key);
        databaseReference2.removeValue();
        Intent intent = new Intent(RelativeBoardContentActivity.this, RelativeBoardActivity.class);
        startActivity(intent);
    }

    public void boardUpdate() {
        databaseReference2 = firebaseDatabase.getReference("board").child("relative").child(key);
        Intent intent = new Intent(getApplicationContext(), RelativeReviseActivity.class);
        intent.putExtra("bordernumber", boardnumber);
        intent.putExtra("key", key);
        startActivityForResult(intent, 1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "게시물이 수정 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
