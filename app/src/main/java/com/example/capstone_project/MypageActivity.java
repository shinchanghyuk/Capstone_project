package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MypageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;  // 리사이클러뷰 선언
    private RecyclerView.LayoutManager layoutManager;  // 레이아웃 매니저 선언
    private RecyclerView.Adapter adapter;   // 리사이클러뷰 어댑터 선언
    private ArrayList<MypageItem> arrayList; // 아이템 담을 배열리스트
    BottomNavigationView bottomNavigationView;  // 바텀 네이게이션 뷰 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private TextView myinfo_tv;
    private FirebaseUser currentUser;
    private GoogleSignInClient googleSignInClient;
    private FirebaseDatabase firebaseDatabase;  // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference databaseReference;    // 파이버에시스 연결(경로) 선언
    private String loginWay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        myinfo_tv = findViewById(R.id.myinfo_tv);
        recyclerView = findViewById(R.id.mypage_recyclerview);
        bottomNavigationView = findViewById(R.id.BottomNavigation);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화
        currentUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        Query query = databaseReference.orderByChild("uid").equalTo(currentUser.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   // 반복문으로 데이터리스트를 추출
                    User userItem = snapshot.getValue(User.class);
                    loginWay = userItem.getLoginWay();
                }
                if (loginWay.equals("구글")) {
                    Log.d("dddd", loginWay);
                    myinfo_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.common_google_signin_btn_icon_light, 0, 0, 0);
                } else {
                    Log.d("dddddd1", loginWay);
                    myinfo_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_facebook_button_icon_blue, 0, 0, 0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_LONG).show();
            }
        });
        myinfo_tv.setText(String.valueOf(" " + currentUser.getDisplayName()) + " 님 환영합니다.");

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //서버의 클라이언트 ID를 메서드에 전달
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        arrayList = new ArrayList<>();
        arrayList.clear();

        List<String> listPage = Arrays.asList(getResources().getStringArray(R.array.mypage));

        for (int i = 0; i < listPage.size(); i++) {
            MypageItem mypageItem = new MypageItem();
            mypageItem.setTitle(listPage.get(i));
            mypageItem.setMoveImage(R.drawable.ic_arrow_forward_black_24dp);
            arrayList.add(mypageItem);
        }

        adapter = new MypageAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_one:
                        Intent intent1 = new Intent(MypageActivity.this, RelativeBoardActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.action_two:

                    case R.id.action_three:

                    case R.id.action_four:
                        Intent intent4 = new Intent(MypageActivity.this, StadiumSelectActivity.class);
                        startActivity(intent4);
                        return true;

                    case R.id.action_five:
                        Intent intent5 = new Intent(MypageActivity.this, MypageActivity.class);
                        startActivity(intent5);
                        return true;
                }
                return false;
            }
        });

        Log.d("ddd", String.valueOf(currentUser.getEmail()));
        Log.d("ddd", String.valueOf(currentUser.getDisplayName()));
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
    }
    public void logout() {
        if (currentUser != null) { // 사용자 객체가 안 비어있다면 화면 이동
            auth.signOut();
            LoginManager.getInstance().logOut();

            googleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void withdrawal() {
        auth.signOut();
        LoginManager.getInstance().logOut();

        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseReference = firebaseDatabase.getReference("users").child(currentUser.getUid());
                        databaseReference.removeValue();
                        Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "회원탈퇴 성공", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}