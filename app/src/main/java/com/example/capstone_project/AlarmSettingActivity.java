package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AlarmSettingActivity extends AppCompatActivity {
    private SwitchCompat relativeSwitch, mercenarySwitch;
    private Button confirm_btn;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference, databaseReference2;  // 파이버에시스 연결(경로) 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private String current_user, current_uid, realarm, mealarm;
    public static String userToken, loginWay, redate, replace, medate, meplace; // 게시글을 작성한 사용자의 uid

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

        confirm_btn = findViewById(R.id.confirm_btn);
        relativeSwitch = findViewById(R.id.relativeSwitch);
        mercenarySwitch = findViewById(R.id.mercenarySwitch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        current_user = firebaseUser.getDisplayName();
        current_uid = firebaseUser.getUid();

        Query query = databaseReference.orderByChild("uid").equalTo(current_uid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    User userItem = snapshot.getValue(User.class);
                    loginWay = userItem.getLoginWay();
                    userToken = userItem.getUserToken();
                    realarm = userItem.getRealarm();
                    mealarm = userItem.getMealarm();
                    redate = userItem.getRedate();
                    replace = userItem.getReplace();
                    medate = userItem.getMedate();
                    meplace = userItem.getMeplace();

                }

                if (realarm.equals("o")) {
                    relativeSwitch.setChecked(true);
                } else {
                    relativeSwitch.setChecked(false);
                }

                if(mealarm.equals("o")) {
                    mercenarySwitch.setChecked(true);
                } else {
                    mercenarySwitch.setChecked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        relativeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    realarm = "o";
                } else {
                    realarm = "x";
                }
                Log.d("dd1", realarm);
            }
        });

        mercenarySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mealarm = "o";
                } else {
                    mealarm = "x";
                }
                Log.d("dd", mealarm);
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(realarm.equals("x")) {
                    redate = "";
                    replace ="";
                }

                if(mealarm.equals("x")) {
                    medate = "";
                    meplace = "";
                }

                databaseReference2 = firebaseDatabase.getReference("users").child(current_uid);

                User user = new User(current_user, current_uid, loginWay, userToken, realarm, mealarm, replace, redate, meplace, medate);
                databaseReference2.setValue(user);

                Toast.makeText(AlarmSettingActivity.this, "알람여부를 설정하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AlarmSettingActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });
    }
}
