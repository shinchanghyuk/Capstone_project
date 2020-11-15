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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private String recom, current_user,key ,current_uid, manager_uid = "", manager_name, alarm_content, alarm_title, comment_uid,
            getTime, commentnum, recommentnum, commentUser, commentTime, commentcontent, recommentCount, fcmToken, fcmUrl, boardnumber,
            serverKey;
    private DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4, databaseReference5, databaseReference6;
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
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        current_user = firebaseUser.getDisplayName();
        current_uid = firebaseUser.getUid();

        fcmUrl = "https://fcm.googleapis.com/fcm/send";
        serverKey = getResources().getString(R.string.server_key);

        databaseReference5 = firebaseDatabase.getReference("manager");
        Query query = databaseReference5.orderByChild("uid").equalTo(current_uid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Manager managerItem = snapshot.getValue(Manager.class);
                    manager_uid = managerItem.getUid();
                    manager_name = managerItem.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });


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
                    comment_uid = commentItem.getUid();
                    boardnumber = commentItem.getBoardnumber();
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

        btnRecomment.setOnClickListener(new View.OnClickListener() {   // 댓글을 작성하는 부분
            @Override
            public void onClick(View view) {

                databaseReference = firebaseDatabase.getReference("board").child("recomment").push();
                databaseReference2 = firebaseDatabase.getReference("user");
                recom = editRecomment.getText().toString(); // 작성한 댓글 내용
                recommentnum = databaseReference.getKey();

                if (recom.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    if(manager_uid.equals(current_uid)) {

                        RecommentItem recommentItem = new RecommentItem(commentnum, boardnumber ,recommentnum, manager_name, getTime, recom, manager_uid);
                        databaseReference.setValue((recommentItem));

                    } else {

                        RecommentItem recommentItem = new RecommentItem(commentnum, boardnumber ,recommentnum, current_user, getTime, recom, current_uid);
                        databaseReference.setValue((recommentItem));

                    }

                    Toast.makeText(getApplicationContext(), "댓글이 작성되었습니다.", Toast.LENGTH_LONG).show();
                    editRecomment.setText("");
                    databaseReference4 = firebaseDatabase.getReference("board").child("comment").child(commentnum);
                    Map<String, Object> boardChange = new HashMap<>();
                    boardChange.put("recomcount", String.valueOf(Integer.parseInt(recommentCount) + 1));
                    databaseReference4.updateChildren(boardChange);

                    alarm_content = "작성하신 댓글에 답글이 달렸습니다.";
                    alarm_content = "댓글 답글 알림";

                    databaseReference6 = firebaseDatabase.getReference("users");
                    Query query = databaseReference6.orderByChild("uid").equalTo(comment_uid);

                    if (!(comment_uid.equals(current_uid))) {
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User userItem = snapshot.getValue(User.class);
                                    fcmToken = userItem.getUserToken();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject root = new JSONObject();
                                                JSONObject notification = new JSONObject();
                                                notification.put("body", alarm_content);
                                                notification.put("title", alarm_title);
                                                root.put("notification", notification);
                                                root.put("to", fcmToken);

                                                URL Url = new URL(fcmUrl);
                                                HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                                // URL 연결
                                                conn.setRequestMethod("POST");
                                                conn.setDoOutput(true);
                                                conn.setDoInput(true);
                                                conn.addRequestProperty("Authorization", "key=" + serverKey);
                                                conn.setRequestProperty("Accept", "application/json");
                                                conn.setRequestProperty("Content-type", "application/json");
                                                OutputStream os = conn.getOutputStream();
                                                os.write(root.toString().getBytes("utf-8"));
                                                os.flush();
                                                conn.getResponseCode();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
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