package com.example.capstone_project.relative;

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
import com.example.capstone_project.User;
import com.example.capstone_project.comment.CommentAdapter;
import com.example.capstone_project.comment.CommentItem;
import com.example.capstone_project.dialog.ConfirmDialog;
import com.example.capstone_project.dialog.ReportDialog;
import com.example.capstone_project.mypage.Manager;
import com.example.capstone_project.report.ReportHistoryActivity;
import com.example.capstone_project.report.ReportItem;
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

public class RelativeBoardContentActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference relative_database, relative_database2, comment_database, comment_database2,
            user_database, manager_database, recomment_database, report_database; // 파이버에시스 연결(경로) 선언
    private TextView matching_tv, place_tv, date_tv, person_tv, ability_tv, content_tv, title_tv, time_tv; // 텍스트 뷰들 선언
    private Button matching_btn, update_btn, delete_btn, list_btn, reply_btn, report_btn, process_btn; // 버튼들 선언
    private String matching, day, title, content, ability, starttime, endtime, person,
            current_user, current_uid, boardnumber, place, commentkey, writeTime, reply, fcmToken,
            alarm_content, alarm_title, fcmUrl, serverKey; // 쓰이는 문자열 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private EditText reply_edit; // 댓글 작성하는 에디트 선언
    private ArrayList<CommentItem> comment_arrayList; // 댓글 아이템 담을 배열리스트
    private RecyclerView recyclerView; // 댓글 리사이클러뷰
    private RecyclerView.LayoutManager layoutManager; // 댓글 리사이클러뷰 레이아웃 매니저
    private RecyclerView.Adapter adapter; // 댓글 리사이클러뷰 어댑터
    public static String manager_uid="", manager_name, uid, reportnum=""; // 관리자의 이름과 uid, 사용자의 uid, 신고 number을 담을 정적변수 선언

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_board_content);

        start(); // 관리자 모드일 때 글쓰기 버튼과 알림 버튼을 안보이게 함
        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 매칭완료 버튼을 눌렀을 때 동작
        matching_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                if (matching.equals("매칭 중")) {
                    dialog.operation("matching1", "relative");
                    // 사용자가 매칭 완료 버튼을 눌렀을 때 확인 다이얼로그를 띄움
                } else if (matching.equals("매칭완료")) {
                    dialog.operation("matching2", "relative");
                    // 사용자가 매칭 취소 버튼을 눌렀을 때 확인 다이얼로그를 띄움
                }
            }
        });

        // 수정 버튼을 눌렀을 때 동작(게시물 작성자 일때)
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                dialog.operation("update", "relative");
                // 작성 사용자가 수정 버튼을 눌렀을 때 확인 다이얼로그를 띄움
            }
        });

        // 삭제 버튼을 눌렀을 때 동작(게시물 작성자 일때)
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                dialog.operation("delete", "relative");
                // 작성 사용자가 삭제 버튼을 눌렀을 때 확인 다이얼로그를 띄움
            }
        });

        // 목록 버튼을 눌렀을 때 동작
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelativeBoardContentActivity.this, RelativeBoardActivity.class);
                startActivity(intent);
                // 상대매칭 게시판 화면으로 이동
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

        // 신고 버튼을 눌렀을 때 동작
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDialog dialog = new ReportDialog(RelativeBoardContentActivity.this);
                dialog.operation("게시판", "relative");
                // 신고 다이얼로그를 띄움
            }
        });

        // 관리자가 조치완료 버튼을 눌렀을 때 동작
        process_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(RelativeBoardContentActivity.this);
                dialog.operation("process", "relative");
                // 관리자가 조치 완료 버튼을 눌렀을 때 확인 다이얼로그를 띄움
            }
        });
    }

    private void start() {
        delete_btn = findViewById(R.id.delete_btn);
        report_btn = findViewById(R.id.report_btn);
        process_btn = findViewById(R.id.process_btn);

        Intent intent = getIntent();
        boardnumber = intent.getStringExtra("boardnumber");
        // 누른 게시글의 번호를 가져와 boardnumber 변수에 넣음

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

        query.addListenerForSingleValueEvent(new ValueEventListener() { // manager 키에 일치하는 uid가 있다면
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Manager managerItem = snapshot.getValue(Manager.class);
                    manager_uid = managerItem.getUid();
                    manager_name = managerItem.getName();
                    // 일치하는 사용자의 name과 uid를 가져옴
                }

                if (!(manager_uid.isEmpty())) {  // 현재 사용자의 uid가 관리자 uid 라면
                    delete_btn.setVisibility(View.VISIBLE);
                    report_btn.setVisibility(View.INVISIBLE);
                    // 삭제버튼을 보이게, 신고버튼을 안보이게 함

                    report_database = firebaseDatabase.getReference("manager").child("board,comment");
                    // manager 안에 있는 board/comment 키에 접근하기 위한 파이어베이스 경로 설정
                    Query query = report_database.orderByChild("boardnumber").equalTo(boardnumber);
                    // board/comment 키에 있는 boardnumber와 지금의 게시물의 boardnumber를 비교

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                                ReportItem reportItem = snapshot.getValue(ReportItem.class);
                                // RelativeBoardItem 객체에 데이터를 담음
                                reportnum = reportItem.getReportnumber();
                                // 이 게시물이 신고가 된 게시물이라면 reportnum을 가져옴
                            }

                            if (!(reportnum.isEmpty())) { // 다른 사용자가 신고한 게시물이라면
                                process_btn.setVisibility(View.VISIBLE);
                                // 조치 완료 버튼을 보이게 함
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
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
        list_btn = findViewById(R.id.list_btn);
        matching_btn = findViewById(R.id.matching_btn);
        update_btn = findViewById(R.id.update_btn);
        reply_btn = findViewById(R.id.reply_btn);
        reply_edit = findViewById(R.id.reply_edit);
        recyclerView = findViewById(R.id.comment_RecyclerView);

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

        relative_database = firebaseDatabase.getReference("board").child("relative");
        // relative 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = relative_database.orderByChild("boardnumber").equalTo(boardnumber);
        // relative 키에 있는 boardnumber와 받아온 boardnumber를 비교

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출
                    RelativeBoardItem relativeBoardItem = snapshot.getValue(RelativeBoardItem.class);
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
                    uid = relativeBoardItem.getUid();
                    // 데이터베이스에서 일치하는 게시물의 정보들을 가져옴
                }

                matching_tv.setText(matching);
                place_tv.setText(place);
                date_tv.setText(day);
                time_tv.setText(starttime + " ~ " + endtime);
                title_tv.setText(title);
                person_tv.setText(person);
                ability_tv.setText(ability);
                content_tv.setText(content);
                // 가져온 정보들을 텍스트 뷰에 보여줌

                if (current_uid.equals(uid)) { // 현재 사용자가 작성 사용자 일때
                    matching_btn.setVisibility(View.VISIBLE);
                    update_btn.setVisibility(View.VISIBLE);
                    delete_btn.setVisibility(View.VISIBLE);
                    report_btn.setVisibility(View.INVISIBLE);
                    // 매칭완료, 수정, 삭제를 보이게 하고, 신고 버튼을 안보이게 함
                }

                if (matching.equals("매칭완료")) { // 이 게시물이 매칭완료 된 게시물이라면
                    matching_btn.setText("매칭취소"); // 매칭완료 버튼을 매칭취소 버튼으로 변경
                } else if (matching.equals("매칭 중")) { // 이 게시물이 매칭 중인 게시풀이라면
                    matching_btn.setText("매칭완료"); // 매칭취소 버튼을 매칭완료 버튼으로 변경
                }
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
        adapter = new CommentAdapter(comment_arrayList, this, "relative");
        // 리사이클러뷰에 CommentAdapter 객체 지정함
        recyclerView.setAdapter(adapter);
        // 리사이클러뷰에 게시물에 달린 댓글 데이터들을 담음
    }

    // 매칭 버튼을 눌렀을 때 동작
    public void matchingChange() {
        if (matching.equals("매칭 중")) { // 매칭 중인 게시물 일 때
            matching = "매칭완료"; // 매칭완료로 변경
            matching_btn.setText("매칭취소"); // 매칭완료 버튼을 매칭취소로 변경
            Toast.makeText(getApplicationContext(), "매칭이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
        } else if (matching.equals("매칭완료")) { // 매칭 완료인 게시물 일 때
            matching = "매칭 중"; // 매칭 중으로 변경
            matching_btn.setText("매칭완료"); // 매칭취소 버튼을 매칭완료로 변경
            Toast.makeText(getApplicationContext(), "매칭이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        relative_database2 = relative_database.child(boardnumber);
        // 해당 게시물을 변경하기 위한 파이어베이스 경로 설정

        Map<String, Object> matchingOk = new HashMap<>();
        matchingOk.put("matching", matching);
        relative_database2.updateChildren(matchingOk);
        matching_tv.setText(matching);
        // 해당 게시물의 데이터 변경
    }

    // 삭제 버튼을 눌렀을 때 동작
    public void boardDelete() {
        relative_database2 = relative_database.child(boardnumber);
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

        manager_database = firebaseDatabase.getReference("manager").child(boardnumber);
        // 만약 신고된 게시물일시 신고 내역을 삭제하기 위한 파이어베이스 경로 설정
        relative_database2.removeValue();
        manager_database.removeValue();
        // 이 게시물에 대한 데이터들을 지움

        Intent intent = new Intent(RelativeBoardContentActivity.this, RelativeBoardActivity.class);
        startActivity(intent);
        // 데이터 정보를 지운 후 상대매칭 게시판 화면으로 이동
    }

    // 수정 버튼을 눌렀을 때 동작
    public void boardUpdate() {
        relative_database2 = relative_database.child(boardnumber);
        // 해당 게시물을 수정하기 위한 파이어베이스 경로 설정
        Intent intent = new Intent(getApplicationContext(), RelativeReviseActivity.class);
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
    private void commentInsert() {
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

            alarm_content = "게시물에 새로운 댓글이 달렸습니다.";
            alarm_title = "상대매칭 게시판 댓글알림";
            // 이 게시물 작성자에게 보낼 알림 제목 및 내용을 변수에 넣음

            user_database = firebaseDatabase.getReference("users");
            // 해당 게시물 작성자에게 댓글이 달렸다는 알림을 보내기 위한 파이어베이스 경로 설정
            Query query = user_database.orderByChild("uid").equalTo(uid);
            // users 키에 있는 사용자들의 uid와 현재 사용자의 uid를 비교

            if (!(uid.equals(current_uid))) { // 게시물을 작성자인 사용자가 댓글을 적었을때 제외한 나머지 경우에 게시물 작성 사용자에게 알림을 전송
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User userItem = snapshot.getValue(User.class);
                            fcmToken = userItem.getUserToken(); // 게시물 작성자 사용자의 알림 토큰을 가져와 변수에 넣음
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
                        }
                    } // 게시물 작성자에게 알림을 보내는 구문

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
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

    // 신고 다이얼로그에서 확인 버튼을 눌렀을 때 동작
    public void report(String type, String boardtype, String radiotype, String content) {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        writeTime = simpleDate.format(mDate);
        // 현재 시간을 출력형식에 맞춘 후 변수에 넣음

        manager_database = firebaseDatabase.getReference("manager").child("board, comment").push();
        // 신고내역을 관리자에게 보내기 위한 파이어베이스 경로 설정
        String reportkey = manager_database.getKey();
        // 신고 데이터 키 값을 변수에 넣음

        ReportItem reportItem = new ReportItem(boardnumber, reportkey, current_user, current_uid, writeTime, radiotype, content, type, boardtype);
        manager_database.setValue(reportItem);
        // 관리자에게 신고 내역을 보내기 위한 파이어베이스 manager 키에 업로드 구문

        Toast.makeText(getApplicationContext(), "신고가 접수 되었습니다.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송
    }

    // 관리자가 조치 완료 버튼을 클릭했을 때
    public void process() {
        report_database.orderByChild("reportnumber").equalTo(reportnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                    // 신고처리가 완료된 게시물이므로 신고 내역 데이터 삭제
                    process_btn.setVisibility(View.INVISIBLE);
                    // 조치완료 버튼은 안보이게 함
                    Toast.makeText(getApplicationContext(), "신고처리가 완료되었습니다.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송

                    Intent intent = new Intent(RelativeBoardContentActivity.this, ReportHistoryActivity.class);
                    startActivity(intent);
                    // 신고 조치를 완료 한 후 ReportHistoryActivity로 이동
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}