package com.example.capstone_project.notice;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.capstone_project.comment.CommentAdapter;
import com.example.capstone_project.comment.CommentItem;
import com.example.capstone_project.dialog.ConfirmDialog;
import com.example.capstone_project.mypage.Manager;
import com.example.capstone_project.relative.RelativeBoardItem;
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

public class NoticeBoardContentActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference manager_database, notice_database, notice_database2,
            comment_database, comment_database2, recomment_database; // 파이버에시스 연결(경로) 선언
    private TextView content_tv, title_tv; // 텍스트 뷰들 선언
    private Button update_btn, delete_btn, list_btn, reply_btn; // 버튼들 선언
    private String title, content, current_user, current_uid, boardnumber, commentkey,
            writeTime, reply, fcmToken, alarm_content, alarm_title, fcmUrl, serverKey; // 쓰이는 문자열 선언
    public static String manager_uid = "", manager_name, uid; // 관리자의 이름과 uid, 신고 number을 담을 정적변수 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private EditText reply_edit; // 댓글 작성하는 에디트 선언
    private ArrayList<CommentItem> comment_arrayList; //댓글 아이템 담을 배열리스트
    private RecyclerView recyclerView; // 댓글 리사이클러뷰
    private RecyclerView.LayoutManager layoutManager; // 댓글 리사이클러뷰 레이아웃 매니저
    private RecyclerView.Adapter adapter; // 댓글 리사이클러뷰 어댑터

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board_content);

        start(); // 관리자 모드일 때 글쓰기 버튼과 알림 버튼을 안보이게 함
        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 수정 버튼을 눌렀을 때 동작(관리자 일때)
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(NoticeBoardContentActivity.this);
                dialog.operation("update", "notice");
                // 관리자가 수정 버튼을 눌렀을 때 확인 다이얼로그를 띄움
            }
        });

        // 삭제 버튼을 눌렀을 때 동작(관리자 일때)
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(NoticeBoardContentActivity.this);
                dialog.operation("delete", "notice");
                // 관리자가 삭제 버튼을 눌렀을 때 확인 다이얼로그를 띄움
            }
        });

        // 목록 버튼을 눌렀을 때 동작
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeBoardContentActivity.this, NoticeBoardActivity.class);
                startActivity(intent);
                // 공지사항 게시판 화면으로 이동

            }
        });

        // 등록 버튼을 눌렀을 때 동작(댓글)
        reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentInsert();
                reply_edit.setText(null);
                // 댓글 등록 메소드로 이동 및 사용자가 작성한 내용 초기화
            }
        });
    }

    private void start() {
        update_btn = findViewById(R.id.update_btn);
        delete_btn = findViewById(R.id.delete_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        auth = FirebaseAuth.getInstance();
        // 파이어베이스 인증 객체 생성
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
        current_user = firebaseUser.getDisplayName();
        current_uid = firebaseUser.getUid();
        // 현재 접속한 사용자의 uid와 이름을 가져옴
        firebaseDatabase = FirebaseDatabase.getInstance();

        manager_database = firebaseDatabase.getReference("manager");
        // manager 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = manager_database.orderByChild("uid").equalTo(current_uid);
        // manager 테이블에 있는 uid와 현재 사용자의 uid를 비교

        query.addListenerForSingleValueEvent(new ValueEventListener() { // manager 테이블에 일치하는 uid가 있다면
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Manager managerItem = snapshot.getValue(Manager.class);
                    manager_uid = managerItem.getUid();
                    manager_name = managerItem.getName();
                    // 일치하는 사용자의 name과 uid를 가져옴
                }

                if (!(manager_uid.isEmpty())) { // 현재 사용자의 uid가 관리자 uid 라면
                    delete_btn.setVisibility(View.VISIBLE);
                    update_btn.setVisibility(View.VISIBLE);
                    // 삭제, 수정 버튼만 보이게 함
                } else {
                    delete_btn.setVisibility(View.INVISIBLE);
                    update_btn.setVisibility(View.INVISIBLE);
                    // 삭제, 수정 버튼만 안보이게 함
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        title_tv = findViewById(R.id.title_tv);
        content_tv = findViewById(R.id.content_tv);
        list_btn = findViewById(R.id.list_btn);
        reply_btn = findViewById(R.id.reply_btn);
        reply_edit = findViewById(R.id.reply_edit);
        recyclerView = findViewById(R.id.comment_RecyclerView);

        Intent intent = getIntent();
        boardnumber = intent.getStringExtra("boardnumber"); // 누른 게시글의 번호

        comment_arrayList = new ArrayList<>();
        // 게시글에 달린 댓글들을 담을 배열리스트 생성
        recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        fcmUrl = "https://fcm.googleapis.com/fcm/send";
        serverKey = getResources().getString(R.string.server_key);
        // 알림을 사용자에게 보내기 위한 url 및 서버키를 변수에 넣음

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate = new SimpleDateFormat("MM월 dd일 hh:mm:ss");
        writeTime = simpleDate.format(mDate);

        notice_database = firebaseDatabase.getReference("board").child("notice");
        // notice 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = notice_database.orderByChild("boardnumber").equalTo(boardnumber);
        // notice 키에 있는 boardnumber와 받아온 boardnumber를 비교

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    Manager noticeBoardItem = snapshot.getValue(Manager.class);
                    // Manager 객체에 데이터를 담음
                    title = noticeBoardItem.getTitle();
                    content = noticeBoardItem.getContent();
                    uid = noticeBoardItem.getUid();
                    // 데이터베이스에서 일치하는 게시물의 정보들을 가져옴
                }
                title_tv.setText(title);
                content_tv.setText(content);
                // 가져온 정보들을 텍스트 뷰에 보여줌
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });

        comment_database = firebaseDatabase.getReference("board").child("comment");
        // comment 키에 접근하기 위한 파이어베이스 경로 설정
        Query query2 = comment_database.orderByChild("boardnumber").equalTo(boardnumber);
        // comment 키에 있는 boardnumber와 받아온 boardnumber를 비교

        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment_arrayList.clear(); // 게시글에 등록된 댓글 데이터들을 담을 배열리스트 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CommentItem commentItem = snapshot.getValue(CommentItem.class);
                    // commentItem 객체에 데이터를 담음
                    comment_arrayList.add(commentItem); // 게시글에 등록된 댓글 데이터들을 배열리스트에 추가

                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
        adapter = new CommentAdapter(comment_arrayList, this, "notice");
        // 리사이클러뷰에 CommentAdapter 객체 지정함
        recyclerView.setAdapter(adapter);
        // 리사이클러뷰에 게시물에 달린 댓글 데이터들을 담음
    }

    // 삭제 버튼을 눌렀을 때 동작
    public void boardDelete() {
        notice_database2 = notice_database.child(boardnumber);
        // 해당 게시물을 삭제하기 위한 파이어베이스 경로 설정

        Query query = comment_database.orderByChild("boardnumber").equalTo(boardnumber);
        // comment 키에 있는 boardnumber와 받아온 boardnumber를 비교
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                    // 해당 댓글 삭제
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recomment_database = firebaseDatabase.getReference("board").child("recomment");
        // 해당 게시물의 달린 댓글의 대댓글을 삭제하기 위한 파이어베이스 경로 설정
        Query query2 = recomment_database.orderByChild("boardnumber").equalTo(boardnumber);
        // recomment 키에 있는 boardnumber와 받아온 boardnumber를 비교
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                    // 해당 대댓글 삭제
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        notice_database2.removeValue();
        // 이 게시물에 대한 데이터들을 지움
        Intent intent = new Intent(NoticeBoardContentActivity.this, NoticeBoardActivity.class);
        startActivity(intent);
        // 데이터 정보를 지운 후 공지사항 게시판 화면으로 이동
    }

    // 수정 버튼을 눌렀을 때 동작
    public void boardUpdate() {
        notice_database2 = notice_database.child(boardnumber);
        // 해당 게시물을 수정하기 위한 파이어베이스 경로 설정
        Intent intent = new Intent(NoticeBoardContentActivity.this, NoticeReviseActivity.class);
        intent.putExtra("boardnumber", boardnumber);
        startActivityForResult(intent, 1);
        // boardnumber 값을 담은 채 게시물 수정 화면으로 이동
    }

    // 게시물을 수정하고 다시 돌아왔을 때 동작
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // 게시물이 수정되고 다시 돌아왔을 때
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "게시물이 수정 되었습니다.", Toast.LENGTH_SHORT).show();
                // 정상적으로 돌아왔을 때 Toast 메세지 전송
            }
        }
    }

    // 등록버튼을 눌렀을 때 동작
    private void commentInsert() { // 댓글 작성 버튼 클릭시 구동 부분
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate1 = new SimpleDateFormat("MM월 dd일 hh:mm:ss");
        writeTime = simpleDate1.format(mDate);
        // 현재 시간을 출력형식에 맞춘 후 변수에 넣음

        reply = reply_edit.getText().toString(); // 사용자가 작성한 댓글을 변수에 넣음
        String replycount = "0"; // 답글의 수를 나타냄(사용자에게 대댓글이 있는지 보여줌)

        comment_database = firebaseDatabase.getReference("board").child("comment");
        // 해당 게시물에 댓글을 등록하기 위한 파이어베이스 경로 설정
        comment_database2 = comment_database.push();
        commentkey = comment_database2.getKey();
        // 이 댓글이 가질 고유한 키를 변수의 넣음

        if (reply.isEmpty()) {  // 댓글 내용을 작성하지 않고 버튼을 눌렀을 때
            Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송
        } else {
            if (manager_uid.equals(current_uid)) {
                CommentItem commentItem = new CommentItem(boardnumber, commentkey, manager_name, writeTime, reply, replycount, manager_uid);
                comment_database2.setValue(commentItem);
                // 댓글 작성자가 관리자 일때 파이어베이스 업로드 구문
            } else {
                CommentItem commentItem = new CommentItem(boardnumber, commentkey, current_user, writeTime, reply, replycount, current_uid);
                comment_database2.setValue(commentItem);
                // 댓글 작성자가 일반 사용자 일때 파이어베이스 업로드 구문
            }
            Toast.makeText(getApplicationContext(), "댓글이 작성 되었습니다.", Toast.LENGTH_SHORT).show(); // 댓글 업로드 후 Toast 메세지 전송

        }
    }

    // 댓글을 삭제하기 위해 댓글을 롱클릭할 때 동작
    public void commentDelete(String commentnum) {
        recomment_database = firebaseDatabase.getReference("board").child("recomment");
        // 삭제하기 위한 댓글에 달린 대댓글을 삭제하기 위한 파이어베이스 경로 설정
        Query query = recomment_database.orderByChild("commentnum").equalTo(commentnum);
        // recomment 키에 있는 commentnum과 받아온 commentnum을 비교

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                    // 해당 대댓글 데이터 삭제
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        comment_database = firebaseDatabase.getReference("board").child("comment").child(commentnum);
        // 해당 을 삭제하기 위한 파이어베이스 경로 설정
        comment_database.removeValue();
        // 댓글 데이터 삭제
    }
}
