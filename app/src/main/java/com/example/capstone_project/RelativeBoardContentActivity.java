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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RelativeBoardContentActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference;    // 파이버에시스 연결(경로) 선언
    private TextView matching_tv, place_tv, date_tv, person_tv, ability_tv, content_tv, title_tv, time_tv;
    private Button list_btn, reply_btn;
    private String matching, day, title, content, ability, starttime, endtime, person;
    private ArrayList<String> place;
    private int number;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_board_content);

        matching_tv = findViewById(R.id.matching_tv);
        place_tv = findViewById(R.id.place_tv);
        date_tv = findViewById(R.id.date_tv);
        time_tv = findViewById(R.id.time_tv);
        person_tv = findViewById(R.id.person_tv);
        ability_tv = findViewById(R.id.ability_tv);
        title_tv = findViewById(R.id.title_tv);
        content_tv = findViewById(R.id.content_tv);
        list_btn = findViewById(R.id.list_btn);
        reply_btn= findViewById(R.id.reply_btn);

        Intent intent = getIntent();
        number = intent.getIntExtra("boardNum", 0);
        Log.d("pp", String.valueOf(number));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("relative");

        databaseReference.orderByChild("number").equalTo(number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    RelativeBoardContentItem relativeBoardItem = snapshot.getValue(RelativeBoardContentItem.class);
                    // RelativeBoardContentItem 객체에 데이터를 담음
                   matching = relativeBoardItem.getMatching();
                   place = relativeBoardItem.getPlace();
                    day = relativeBoardItem.getDay();
                    title = relativeBoardItem.getTitle();
                    content = relativeBoardItem.getContent();
                    ability = relativeBoardItem.getAbility();
                    starttime = relativeBoardItem.getStarttime();
                    endtime = relativeBoardItem.getEndtime();
                    person = relativeBoardItem.getPerson();
                }

                matching_tv.setText(matching);
                place_tv.setText(String.valueOf(place));
                date_tv.setText(day);
                time_tv.setText(starttime + " ~ " + endtime);
                title_tv.setText(title);
                person_tv.setText(person);
                ability_tv.setText(ability);
                content_tv.setText(content);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
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
}
