package com.example.capstone_project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_project.dialog.ConfirmDialog;
import com.example.capstone_project.mercenary.MercenaryBoardActivity;
import com.example.capstone_project.mypage.MypageActivity;
import com.example.capstone_project.relative.RelativeBoardActivity;
import com.example.capstone_project.stadium.StadiumDetailsActivity;
import com.example.capstone_project.stadium.StadiumSelectActivity;
import com.example.capstone_project.team.TeamBoardActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    private Button menu_btn[]; // 버튼들 선언
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private String current_uid; // 현재 접속한 사용자의 uid
    private GoogleSignInClient googleSignInClient; // 구글 API 클라이언트 객체
    private long backKeyPressedTime = 0;
    private Toast toast; // 뒤로 가기 이벤트와 관련하여 사용자에게 알려주기

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_select);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        menu_btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RelativeBoardActivity.class);
                startActivity(intent);
                // 상대매칭 버튼을 선택했을 때
            }
        });

        menu_btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MercenaryBoardActivity.class);
                startActivity(intent);
                // 용병모집 버튼을 선택했을 때
            }
        });

        menu_btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, StadiumSelectActivity.class);
                startActivity(intent);
                // 구장정보 버튼을 선택했을 때
            }
        });

        menu_btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TeamBoardActivity.class);
                startActivity(intent);
                // 팀 홍보 버튼을 선택했을 때
            }
        });

        menu_btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MypageActivity.class);
                startActivity(intent);
                // 환경설정 버튼을 선택했을 때
            }
        });

        menu_btn[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(MenuActivity.this);
                dialog.operation("logout", "menu");
                // 로그아웃 버튼을 선택했을 때
            }
        });
    }
    public void init() {
        menu_btn = new Button[6];

        Integer btn_id[] = {R.id.Relative_btn, R.id.Mercenary_btn, R.id.Stadium_btn,
                R.id.Promotion_btn, R.id.Declaration_btn, R.id.logout_btn};

        for (int i = 0; i < menu_btn.length; i++) {
            menu_btn[i] = findViewById(btn_id[i]);
        }

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
        current_uid = firebaseUser.getUid();
        // 현재 접속한 사용자의 uid를 가져옴

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //서버의 클라이언트 ID를 메서드에 전달
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    }
    // 로그아웃 버튼을 눌렀을 때 동작
    public void logout() {
        if (current_uid!= null) {
            auth.signOut();
            // 파이어베이스 인증객체 로그아웃
            LoginManager.getInstance().logOut();
            // 사용자가 페이스북 로그인 시 로그아웃 동작

            googleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "로그아웃이 성공적으로 마무리되었습니다.", Toast.LENGTH_SHORT).show();
                            // 로그아웃 후 MainActivity로 이동 및 Toast 메세지 전송
                        }
                    });
            // 사용자가 구글 로그인 시 로그아웃 동작
        }
    }

    // 뒤로 가기 이벤트가 발생되었을 때 동작
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show(); // Toast 메세지 전소
            return;
            // 사용자가 뒤로가기 버튼을 한번 눌렀을 때 동작
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel(); // 전에 동작되던 Toast 메세지 취소
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show(); // 새로운 Toast 메세지 전송
            // 사용자가 뒤로가기를 2초안에 두번 눌렀을 때 동작

            finishAffinity(); // 애플리케이션을 종료함
            System.runFinalization(); // 현재 작업중인 쓰레드가 다 종료되면, 종료
        }
    }
}