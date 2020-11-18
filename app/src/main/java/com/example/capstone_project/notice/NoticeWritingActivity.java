package com.example.capstone_project.notice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_project.R;
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
import java.util.Date;

public class NoticeWritingActivity extends AppCompatActivity {

    private Button confirm_btn; // 버튼들 선언
    private EditText title_edit, content_edit; // 사용자가 게시물을 작성 시 필요한 제목, 내용 edit 선언
    private String title, content, boardnumber, writetime, current_uid,
            alarm_title, alarm_content, fcmUrl, serverKey, fcmToken; // 쓰이는 문자열 선언
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference notice_database, manager_database, user_database; // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    public static String current_user=""; // 관리자의 이름을 담을 정적변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_writing);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 확인 버튼을 눌렀을 때 동작
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managerdata();
                // managerdata 메소드 호출
            }
        });
    }
    private void init() {
        confirm_btn = findViewById(R.id.btnUp);
        title_edit = findViewById(R.id.title_edit);
        content_edit = findViewById(R.id.content_edit);

        fcmUrl = "https://fcm.googleapis.com/fcm/send";
        serverKey = getResources().getString(R.string.server_key);
        // 알림을 사용자에게 보내기 위한 url 및 서버키를 변수에 넣음
    }
    private void managerdata() {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
        writetime = sdf.format(mDate);
        // 현재 시간을 출력형식에 맞춘 후 변수에 넣음

        title = title_edit.getText().toString();
        content = content_edit.getText().toString();
        // 사용자가 입력한 제목, 내용들을 변수에 넣음

        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        notice_database = firebaseDatabase.getReference("board").child("notice").push();
        // 해당 게시물을 notice 키에 새로 넣기 위한 파이어베이스 경로 설정
        boardnumber = notice_database.getKey();
        // 이 게시물의 키를 boardnumber 변수에 넣음
        auth = FirebaseAuth.getInstance();
        // 파이어베이스 인증 객체 생성
        FirebaseUser user = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
        current_uid = user.getUid();
        // 현재 접속한 사용자의 uid을 가져옴

        alarm_title = "공지사항 알림";
        alarm_content = "공지사항이 등록되었습니다.";
        // 사용자에게 보낼 알림 제목 및 내용을 변수에 넣음

        manager_database = firebaseDatabase.getReference("manager");
        // manager 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = manager_database.orderByChild("uid").equalTo(current_uid);
        // manager 키에 있는 uid와 현재 사용자의 uid를 비교

        query.addListenerForSingleValueEvent(new ValueEventListener() { // manager 키에 일치하는 uid가 있다면
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Manager managerItem = snapshot.getValue(Manager.class);
                    current_user = managerItem.getName();
                    // 데이터베이스에서 일치하는 게시물의 정보를 가져옴
                }

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
                    // 사용자가 빈칸없이 선택하지 않았을 때 Toast 메세지 전송
                } else {
                    // 사용자가 모두 알맞게 입력 하였을 때
                    Manager managerItem = new Manager(current_user, current_uid, boardnumber, title, writetime, content);
                    notice_database.setValue(managerItem);
                    // 해당 게시글 파이어베이스 업로드 구문

                    Toast.makeText(getApplicationContext(), "게시물이 작성 되었습니다.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송

                    user_database = firebaseDatabase.getReference("users");
                    // 공지사항을 알림을 받겠다고 한 유저들에게 알림을 보내기 위한 파이어베이스 경로 설정
                    Query query = user_database.orderByChild("noticealarm").equalTo("o");
                    // users 키에 있는 사용자들의 noticealarm의 값이 o 인지 비교

                    query.addListenerForSingleValueEvent(new ValueEventListener() { // 알림 조건이 일치하는 사용자들에게 알림을 전송
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User userItem = snapshot.getValue(User.class);
                                    fcmToken = userItem.getUserToken(); // 해당되는 사용자들의 알림 토큰을 가져와 변수에 넣음
                                    Log.d("token", fcmToken);
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
                                                URL Url = new URL(fcmUrl); // URL 연결
                                                HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
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
                            } // 해당되는 사용자들에게 알림을 보내는 구문

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
    }
}
