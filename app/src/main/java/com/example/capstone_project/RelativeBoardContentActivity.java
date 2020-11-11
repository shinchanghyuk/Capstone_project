package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RelativeBoardContentActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2, databaseReference3;    // 파이버에시스 연결(경로) 선언
    private TextView matching_tv, place_tv, date_tv, person_tv, ability_tv, content_tv, title_tv, time_tv;
    private Button matching_btn, update_btn, delete_btn, list_btn, reply_btn;
    private String matching, day, title, content, ability, starttime, endtime, person, user, current_user, boardnumber, key, place, replytxt, getTime, commentnum;
    private EditText reply_edit;
    private RecyclerView recyclerView; // 댓글 리사이클러뷰
    private RecyclerView.LayoutManager layoutManager; //댓글 리사이클러뷰 레이아웃 매니저
    private RecyclerView.Adapter adapter; //댓글 리사이클러뷰 어댑터
    private ArrayList<CommentItem> arrayList; //댓글 아이템 담을 배열리스트
    private FirebaseAuth auth; // 파이어베이스 인증 객체

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_board_content);

        init();

        databaseReference = firebaseDatabase.getReference("board").child("relative");

        //댓글 작성시에도 필요해서 메소드 밖으로 꺼내놓음
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        current_user = firebaseUser.getDisplayName();

        Log.d("number", boardnumber);

        Query query = databaseReference.orderByChild("boardnumber").equalTo(boardnumber);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    RelativeBoardItem relativeBoardItem = snapshot.getValue(RelativeBoardItem.class);
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

                /*auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                current_user = firebaseUser.getDisplayName();*/

                if (current_user.equals(user)) {
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
                if (matching.equals("매칭 중")) {
                    dialog.operation("matching1", "relative");
                } else if (matching.equals("매칭완료")) {
                    dialog.operation("matching2", "relative");
                }
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                dialog.operation("update", "relative");
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                dialog.operation("delete", "relative");
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
                upcommnet();
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
        reply_btn = findViewById(R.id.reply_btn);
        reply_edit = findViewById(R.id.reply_edit);
        recyclerView = findViewById(R.id.comment_RecyclerView);

        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        boardnumber = intent.getStringExtra("boardnumber"); // 누른 게시글의 번호

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM월 dd일 hh:mm:ss"); //수정(김)
        getTime = simpleDate.format(mDate);

        arrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(10);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference2 = firebaseDatabase.getReference("board").child("comment");
        databaseReference2.orderByChild("boardnumber").equalTo(boardnumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CommentItem commentItem = snapshot.getValue(CommentItem.class);
                    arrayList.add(commentItem);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
        adapter = new CommentAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);


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

    public void commentDelete(String comnum) {

        databaseReference2 = firebaseDatabase.getReference("board").child("recomment");
        databaseReference2.orderByChild("commentnum").equalTo(comnum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference2 = firebaseDatabase.getReference("board").child("comment").child(comnum);
        databaseReference2.removeValue();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "게시물이 수정 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void upcommnet() { //댓글 작성 버튼 클릭시 구동 부분
        databaseReference3 = firebaseDatabase.getReference("board").child("comment").push();

        replytxt = reply_edit.getText().toString(); // 작성한 글
        String replycount = "0"; //첫 댓글 작성시 답글 수 기본값 0으로 넣어주기
        commentnum = databaseReference3.getKey();

        if (replytxt.isEmpty()) {
            Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show(); //입력을 하지 않고 버튼을 눌렀을때
        } else {

            CommentItem commentItem = new CommentItem(boardnumber, commentnum, current_user, getTime, replytxt, replycount);
            databaseReference3.setValue((commentItem)); //파이어베이스 업로드 구문
            Toast.makeText(getApplicationContext(), "댓글이 작성 되었습니다.", Toast.LENGTH_SHORT).show();
            reply_edit.setText("");
        }

    }
}
