package com.example.capstone_project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    private Button menu_btn[];
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private FirebaseUser currentUser;
    private GoogleSignInClient googleSignInClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_select);

        myHelper = new MyDBHelper(this);
        menu_btn = new Button[6];

        Integer btn_id[] = {R.id.Relative_btn, R.id.Mercenary_btn, R.id.Stadium_btn,
                R.id.Promotion_btn, R.id.Declaration_btn, R.id.logout_btn};

        for (int i = 0; i < menu_btn.length; i++) {
            menu_btn[i] = findViewById(btn_id[i]);
        }

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화
        currentUser = auth.getCurrentUser();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //서버의 클라이언트 ID를 메서드에 전달
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        menu_btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RelativeBoardActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MercenaryBoardActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, StadiumSelectActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TeamBoardActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });

        menu_btn[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(MenuActivity.this);
                dialog.operation("logout", "menu");
            }
        });
    }

    public void logout() {
        if (currentUser != null) { // 사용자 객체가 안 비어있다면 화면 이동
            auth.signOut();
            LoginManager.getInstance().logOut();

            googleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "로그아웃이 성공적으로 마무리되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}