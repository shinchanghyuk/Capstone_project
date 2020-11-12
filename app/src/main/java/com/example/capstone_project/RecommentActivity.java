package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
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

public class RecommentActivity extends AppCompatActivity {
    private Button btnRecomment;
    private EditText editRecomment;
    private TextView txtUser, txtTime, txtComment;
    private RecyclerView recyclerView; //대댓글 리사이클러뷰
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<RecommentItem> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private String recom;
    private String getTime, commentnum, recommentnum, commentUser, commentTime, commentcontent, recommentCount, current_user, key;
    private DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4;
    private FirebaseAuth auth;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recomment);

        txtUser = findViewById(R.id.txtUser);
        txtTime = findViewById(R.id.txtTime);
        txtComment = findViewById(R.id.txtComment);
        btnRecomment = findViewById(R.id.btnRecomment);
        editRecomment = findViewById(R.id.editRecomment);
        recyclerView = findViewById(R.id.recomment);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(10);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setLayoutManager(layoutManager);

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM월 dd일 hh:mm:ss");
        getTime = simpleDate.format(mDate);

        firebaseDatabase = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        current_user = firebaseUser.getDisplayName();

        Intent intent = getIntent();
        commentnum = intent.getStringExtra("commentnum");

        databaseReference3 = firebaseDatabase.getReference("board").child("comment");
        databaseReference3.orderByChild("commentnum").equalTo(commentnum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CommentItem commentItem = snapshot.getValue(CommentItem.class);

                    key = snapshot.getKey();
                    commentUser = commentItem.getUser();
                    commentTime = commentItem.getWritetime();
                    commentcontent = commentItem.getContent();
                    recommentCount = commentItem.getRecomcount();
                }

                txtUser.setText(commentUser);
                txtComment.setText(commentcontent);
                txtTime.setText(commentTime);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();

            }
        });


        arrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference2 = firebaseDatabase.getReference("board").child("recomment");


        databaseReference2.orderByChild("commentnum").equalTo(commentnum).addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RecommentItem recommentItem = snapshot.getValue(RecommentItem.class);
                    arrayList.add(recommentItem);
                }
                adapter.notifyDataSetChanged();
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        adapter = new RecommentAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        btnRecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = firebaseDatabase.getReference("board").child("recomment").push();
                recom = editRecomment.getText().toString();
                recommentnum = databaseReference.getKey();

                if (recom.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else {

                    RecommentItem recommentItem = new RecommentItem(commentnum, recommentnum, current_user, getTime, recom);
                    databaseReference.setValue((recommentItem));
                    Toast.makeText(getApplicationContext(), "댓글이 작성되었습니다.", Toast.LENGTH_LONG).show();
                    editRecomment.setText("");

                    databaseReference4 = firebaseDatabase.getReference("board").child("comment").child(commentnum);
                    Map<String, Object> boardChange = new HashMap<>();
                    boardChange.put("recomcount",String.valueOf(Integer.parseInt(recommentCount) + 1));
                    databaseReference4.updateChildren(boardChange);
                }
            }
        });
    }

    public void commentDelete(String recomnum) {

        databaseReference2 = firebaseDatabase.getReference("board").child("recomment").child(recomnum);
        databaseReference2.removeValue();
        databaseReference4 = firebaseDatabase.getReference("board").child("comment").child(commentnum);
        Map<String, Object> boardChange = new HashMap<>();
        boardChange.put("recomcount",String.valueOf(Integer.parseInt(recommentCount) -1));
        databaseReference4.updateChildren(boardChange);

    }
}
// 관리자 인데 이름 나옴