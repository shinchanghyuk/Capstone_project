package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManagerWritingActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private Button confirm_btn;
    private EditText title_edit, content_edit;
    private String title, number ="", manager_user, manager_uid, content, boardnumber, day,
            alarm_title, alarm_content, fcmUrl, serverKey, fcmToken;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;    // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_writing);

        init();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("board").child("relative").push();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        manager_user = "관리자";
        manager_uid = user.getUid();

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managerdata();
            }
        });
    }
    private void init() {
        confirm_btn = findViewById(R.id.btnUp);
        title_edit = findViewById(R.id.title_edit);
        content_edit = findViewById(R.id.content_edit);

        fcmUrl = "https://fcm.googleapis.com/fcm/send";
        serverKey = getResources().getString(R.string.server_key);

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
        day = simpleDate.format(mDate);

        //
    }
    private void managerdata() {
        title = title_edit.getText().toString();
        content = content_edit.getText().toString();
        boardnumber = databaseReference.getKey();

        alarm_title = "공지사항 알림";
        alarm_content = "공지사항이 등록되었습니다.";

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getApplicationContext(), "빈칸 없이 모두다 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            Manager managerItem = new Manager(manager_user, manager_uid, boardnumber);

            databaseReference.setValue(managerItem);
            Toast.makeText(getApplicationContext(), "게시물이 작성 되었습니다.", Toast.LENGTH_SHORT).show();

            databaseReference2 = firebaseDatabase.getReference("users");

            Query query = databaseReference2.orderByChild("uid");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User userItem = snapshot.getValue(User.class);
                        if (userItem.getNoticealarm().equalsIgnoreCase("o")) {
                            fcmToken = userItem.getUserToken();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Log.d("ddd", fcmToken);
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            finish();
        }
    }
}
