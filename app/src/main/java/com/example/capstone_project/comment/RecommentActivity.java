package com.example.capstone_project.comment;

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

import com.example.capstone_project.R;
import com.example.capstone_project.RecyclerDecoration;
import com.example.capstone_project.User;
import com.example.capstone_project.mypage.Manager;
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
    private Button btnRecomment; // 버튼들 선언
    private EditText editRecomment; // 사용자가 작성한 대댓글의 내용을 담을 edit 선언
    private TextView txtUser, txtTime, txtComment; // 텍스트 뷰들 선언
    private RecyclerView recyclerView; // 대댓글 리사이클러뷰
    private RecyclerView.LayoutManager layoutManager; // 대댓글 리사이클러뷰 레이아웃 매니저
    private RecyclerView.Adapter adapter; // 대댓글 리사이클러뷰 어댑터
    private ArrayList<RecommentItem> arrayList; // 대댓글 아이템 담을 배열리스트
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private String recontent, current_user, current_uid, alarm_content, alarm_title, comment_uid,
            writetime, commentnum, recommentnum, commentUser, commentTime, commentcontent, recommentCount,
            fcmToken, fcmUrl, boardnumber, serverKey; // 쓰이는 문자열 선언
    private DatabaseReference recomment_database, recomment_database2, manager_database,
            comment_database, comment_database2, user_database; // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    public static String manager_uid="", manager_name; // 관리자의 이름과 uid을 담을 정적변수 선언

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recomment);

        start(); // 지금 사용자가 관리자 인지 확인하는 메소드
        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 등록 버튼을 눌렀을 때 동작
        btnRecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);

                SimpleDateFormat simpleDate1 = new SimpleDateFormat("MM월 dd일 hh:mm:ss");
                writetime = simpleDate1.format(mDate);
                // 현재 시간을 출력형식에 맞춘 후 변수에 넣음

                recomment_database = firebaseDatabase.getReference("board").child("recomment").push();
                recontent = editRecomment.getText().toString(); // 작성한 댓글 내용
                recommentnum = recomment_database.getKey();

                if (recontent.isEmpty()) {
                    // 사용자가 댓글내용을 적지 않았을 때
                    Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    if(manager_uid.equals(current_uid)) {
                        RecommentItem recommentItem = new RecommentItem(commentnum, boardnumber ,recommentnum, manager_name, writetime, recontent, manager_uid);
                        recomment_database.setValue(recommentItem);
                        // 댓글 작성자가 관리자 일때 파이어베이스 업로드 구문
                    } else {
                        RecommentItem recommentItem = new RecommentItem(commentnum, boardnumber ,recommentnum, current_user, writetime, recontent, current_uid);
                        recomment_database.setValue(recommentItem);
                        // 댓글 작성자가 일반 사용자 일때 파이어베이스 업로드 구문
                    }

                    Toast.makeText(getApplicationContext(), "댓글이 작성되었습니다.", Toast.LENGTH_LONG).show(); // 댓글 업로드 후 Toast 메세지 전송
                    editRecomment.setText(""); // 사용자가 작성한 내용 초기화

                    comment_database = firebaseDatabase.getReference("board").child("comment").child(commentnum);
                    Map<String, Object> countChange = new HashMap<>();
                    countChange.put("recomcount", String.valueOf(Integer.parseInt(recommentCount) + 1));
                    comment_database.updateChildren(countChange);

                    alarm_content = "작성하신 댓글에 답글이 달렸습니다.";
                    alarm_title = "댓글답글 알림";
                    // 댓글을 작성한 사용자에게 보낼 알림 제목 및 내용을 변수에 넣음

                    user_database= firebaseDatabase.getReference("users");
                    // 해당 댓글 작성자에게 대댓글이 달렸다는 알림을 보내기 위한 파이어베이스 경로 설정
                    Query query = user_database.orderByChild("uid").equalTo(current_uid);
                    // users 키에 있는 사용자들의 uid와 현재 사용자의 uid를 비교

                    if (!(comment_uid.equals(current_uid))) {
                        // 댓글을 작성한 사용자가 대댓글을 적었을때 제외한 나머지 사용자가 댓글을 작성했을 때 알림을 전송
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User userItem = snapshot.getValue(User.class);
                                    fcmToken = userItem.getUserToken(); // 댓글 작성 사용자의 알림 토큰을 가져와 변수에 넣음
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
                                                HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL 연결
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
                                } // 알림을 보내는 구문
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
    public void start() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        auth = FirebaseAuth.getInstance();
        // 파이어베이스 인증 객체 생성
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
        current_user = firebaseUser.getDisplayName();
        current_uid = firebaseUser.getUid();
        // 현재 접속한 사용자의 uid와 이름을 가져옴

        manager_database = firebaseDatabase.getReference("manager");
        // manager 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = manager_database.orderByChild("uid").equalTo(current_uid);
        // manager 테이블에 있는 uid와 현재 사용자의 uid를 비교

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Manager managerItem = snapshot.getValue(Manager.class);
                    manager_uid = managerItem.getUid();
                    manager_name = managerItem.getName();
                    // 일치하는 사용자의 name과 uid를 가져옴
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void init() {
        txtUser = findViewById(R.id.txtUser);
        txtTime = findViewById(R.id.txtTime);
        txtComment = findViewById(R.id.txtComment);
        btnRecomment = findViewById(R.id.btnRecomment);
        editRecomment = findViewById(R.id.editRecomment);
        recyclerView = findViewById(R.id.recomment);

        Intent intent = getIntent();
        commentnum = intent.getStringExtra("commentnum");
        // 해당 게시물의 댓글번호를 가져와 commentnum 변수에 넣음

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(10);
        recyclerView.addItemDecoration(spaceDecoration);

        arrayList = new ArrayList<>();
        // 대댓글들을 담을 배열리스트 생성
        recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        fcmUrl = "https://fcm.googleapis.com/fcm/send";
        serverKey = getResources().getString(R.string.server_key);
        // 알림을 사용자에게 보내기 위한 url 및 서버키를 변수에 넣음

        comment_database = firebaseDatabase.getReference("board").child("comment");
        // comment 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = comment_database.orderByChild("commentnum").equalTo(commentnum);
        // comment 키에 있는 commentnum과 받아온 commentnum를 비교

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CommentItem commentItem = snapshot.getValue(CommentItem.class);
                    // commentItem 객체에 데이터를 담음
                    commentUser = commentItem.getUser();
                    commentTime = commentItem.getWritetime();
                    commentcontent = commentItem.getContent();
                    recommentCount = commentItem.getRecomcount();
                    comment_uid = commentItem.getUid();
                    boardnumber = commentItem.getBoardnumber();
                    // 데이터베이스에서 일치하는 해당 댓글의 데이터들을 가져옴
                }

                txtUser.setText(commentUser);
                txtComment.setText(commentcontent);
                txtTime.setText(commentTime);
                // 가져온 정보들을 텍스트 뷰에 보여줌
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();

            }
        });

        recomment_database = firebaseDatabase.getReference("board").child("recomment");
        // recomment 키에 접근하기 위한 파이어베이스 경로 설정
        Query query2 = recomment_database.orderByChild("commentnum").equalTo(commentnum);
        // recomment 키에 있는 commentnum과 받아온 commentnum를 비교
        query2.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear(); // 댓글에 등록된 대댓글 데이터들을 담을 배열리스트 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RecommentItem recommentItem = snapshot.getValue(RecommentItem.class);
                    // RecommentItem 객체에 데이터를 담음
                    arrayList.add(recommentItem); // 댓글에 등록된 대댓글 데이터들을 배열리스트에 추가
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        adapter = new RecommentAdapter(arrayList, this);
        // 리사이클러뷰에 RecommentAdapter 객체 지정함
        recyclerView.setAdapter(adapter);
        // 리사이클러뷰에 댓글에 달린 대댓글 데이터들을 담음

    }

    // 대댓글을 삭제하기 위해 대댓글을 롱클릭할 때 동작
    public void commentDelete(String recomnum) {

        recomment_database2 = recomment_database.child(recomnum);
        // 대댓글을 삭제하기 위한 파이어베이스 경로 설정
        recomment_database2.removeValue();
        // 해당 대댓글 데이터 삭제
        comment_database2 = comment_database.child(commentnum);
        // 해당 댓글의 답글의 수를 변경하기 위한 파이어베이스 경로 설정
        Map<String, Object> boardChange = new HashMap<>();
        boardChange.put("recomcount",String.valueOf(Integer.parseInt(recommentCount) -1));
        comment_database2.updateChildren(boardChange);
        // 답글의 수 변경
    }
}