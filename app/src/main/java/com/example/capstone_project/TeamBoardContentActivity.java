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

public class TeamBoardContentActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;    // 파이버에시스 연결(경로) 선언
    private TextView matching_tv, place_tv, date_tv, person_tv, ability_tv, content_tv, title_tv, name_tv;
    private Button update_btn, delete_btn, list_btn, reply_btn;
    private String matching, day, title, content, ability, name, person, user, current_user, boardnumber, key, place;
    private FirebaseAuth auth; // 파이어베이스 인증 객체

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_board_content);

        init();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("team");

        Log.d("number", boardnumber);

        Query query = databaseReference.orderByChild("boardnumber").equalTo(boardnumber);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    TeamBoardItem teamBoardItem = snapshot.getValue(TeamBoardItem.class);
                    // TeamBoardItem 객체에 데이터를 담음
                    key = snapshot.getKey();
                    matching = teamBoardItem.getMatching();
                    place = teamBoardItem.getPlace();
                    day = teamBoardItem.getDay();
                    title = teamBoardItem.getTitle();
                    name = teamBoardItem.getName();
                    content = teamBoardItem.getContent();
                    ability = teamBoardItem.getAbility();
                    person = teamBoardItem.getPerson();
                    user = teamBoardItem.getUser();
                }

                matching_tv.setText(matching);
                place_tv.setText(place);
                date_tv.setText(day);
                title_tv.setText(title);
                name_tv.setText(name);
                person_tv.setText(person);
                ability_tv.setText(ability);
                content_tv.setText(content);

                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                current_user = firebaseUser.getDisplayName();

                if(current_user.equals(user)) {
                update_btn.setVisibility(View.VISIBLE);
                delete_btn.setVisibility(View.VISIBLE);
            }
        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(TeamBoardContentActivity.this);
                dialog.operation("update", "team");
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(TeamBoardContentActivity.this);
                dialog.operation("delete","team");
            }
        });

        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamBoardContentActivity.this, TeamBoardActivity.class);
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
        name_tv = findViewById(R.id.name_tv);
        person_tv = findViewById(R.id.person_tv);
        ability_tv = findViewById(R.id.ability_tv);
        title_tv = findViewById(R.id.title_tv);
        content_tv = findViewById(R.id.content_tv);
        update_btn = findViewById(R.id.update_btn);
        delete_btn = findViewById(R.id.delete_btn);
        list_btn = findViewById(R.id.list_btn);
        reply_btn= findViewById(R.id.reply_btn);

        Intent intent = getIntent();
        boardnumber = intent.getStringExtra("boardnumber"); // 누른 게시글의 번호
    }
    public void boardDelete() {
        databaseReference2 = firebaseDatabase.getReference("board").child("team").child(key);
        databaseReference2.removeValue();
        Intent intent = new Intent(TeamBoardContentActivity.this, TeamBoardActivity.class);
        startActivity(intent);
}

    public void boardUpdate() {
        databaseReference2 = firebaseDatabase.getReference("board").child("team").child(key);
        Intent intent = new Intent(getApplicationContext(), TeamReviseActivity.class);
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
