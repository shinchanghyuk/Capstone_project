package com.example.capstone_project;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_project.MenuActivity;
import com.example.capstone_project.R;
import com.example.capstone_project.User;
import com.example.capstone_project.mypage.Manager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private SignInButton google_login_btn; // 구글 로그인 버튼
    private LoginButton facebook_login_btn; // 페이스북 로그인 버튼
    private CallbackManager mCallbackManager; // 콜백
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int SIGN_Google = 100; // 구글 로그인 결과 코드
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference user_database, user_database2, manager_database; // 파이버에시스 연결(경로) 선언
    private String loginWay, userToken, manager_uid, manager_name,
            userUid, realarm, mealarm, noticealarm; // 쓰이는 문자열 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 구글 계정 로그인 버튼을 눌렀을 때 동작
        google_login_btn.setOnClickListener(new View.OnClickListener() { // 구글 로그인 버튼 클릭
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_Google);
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        // 로그인 응답을 처리할 콜백 관리자를 만듬

        facebook_login_btn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) { // 사용자가 정상적으로 로그인 했을 때
                handleFacebookAccessToken(loginResult.getAccessToken());
                // 로그인한 사용자의 액세스 토큰을 가져옴
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "페이스북 로그인을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                // 로그인이 취소 되었을 때 Toast 메세지 전송
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init() {
        google_login_btn = findViewById(R.id.google_login_btn);
        facebook_login_btn = (LoginButton) findViewById(R.id.facebook_login_btn);

        TextView textView = (TextView) google_login_btn.getChildAt(0);
        textView.setText("구글계정으로 로그인");
        textView.setTextSize(Dimension.SP, 15);
        // 구글 계정 로그인 버튼을 커스텀 하기 위해서 사용

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //서버의 클라이언트 ID를 메서드에 전달
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화
        firebaseUser = auth.getCurrentUser(); // 현재 로그인 된 사용자의 객체를 가져옴

        if (firebaseUser != null) { // 사용자 객체가 안 비어있다면 화면 이동
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            // MenuActivity로 이동
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 구글 로그인 인증을 요청했을 때 결과 값을 되돌려 받는 곳
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_Google) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) { // 인증을 성공 하였다면
                GoogleSignInAccount account = result.getSignInAccount(); // 구글 로그인의 모든 정보를 담고 있음
                resultLogin(account); // 로그인 결과 값 출력 수행하라는 메소드
            }
        }

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // 페이스북 로그인 요청 시 onActivityResult() 메소드로 들어오게 됨
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 로그인이 성공 되었을 때
                            loginWay = "구글";
                            // 구글이라는 문자열을 변수에 넣음
                            userdata(loginWay);
                            // userdata 메소드 호출
                        } else {
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            // 실패 시 Toast 메세지 전송
                        }
                    }
                });
    }

    // Firebase 사용자 인증 정보로 교환하고 해당 정보를 사용해 Firebase 인증 할때 동작
    private void handleFacebookAccessToken(final AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 로그인이 성공 하였을 때
                            loginWay = "페이스북";
                            // 페이스 북이라는 문자열을 변수에 넣음
                            userdata(loginWay);
                            // userdata 메소드 호출
                        } else {
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            // 실패 시 Toast 메세지 전송
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void userdata(final String loginWay) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        // 파이어베이스 데이터베이스 객체 생성
        firebaseUser = auth.getCurrentUser();
        // 현재 로그인 된 사용자의 객체를 가져옴

        manager_database = firebaseDatabase.getReference("manager");
        // manager 키에 접근하기 위한 파이어베이스 경로 설정
        Query query = manager_database.orderByChild("uid").equalTo(firebaseUser.getUid());
        // manager 키에 있는 uid와 현재 사용자의 uid를 비교

        query.addListenerForSingleValueEvent(new ValueEventListener() { // manager 테이블에 일치하는 uid가 있다면
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Manager managerItem = snapshot.getValue(Manager.class);
                    manager_name = managerItem.getName();
                    manager_uid = managerItem.getUid();
                    // 일치하는 사용자의 uid를 가져옴
                }
                if (manager_uid != null) { // 현재 로그인한 사용자가 관리자라면
                    Toast.makeText(MainActivity.this, manager_name + " 님 환영합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    // 환영한다는 Toast 메세지를 전송하며 MenuActivity로 이동
                } else { // 현재 로그인한 사용자가 일반 사용자라면
                    user_database = firebaseDatabase.getReference("users");
                    // users 키에 접근하기 위한 파이어베이스 경로 설정
                    Query query = user_database.orderByChild("uid").equalTo(firebaseUser.getUid());
                    // users 키에 있는 uid와 현재 사용자의 uid를 비교

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User userItem = snapshot.getValue(User.class);
                                userUid = userItem.getUid();
                                // uid의 데이터가 있으면 기존 사용자
                            }

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    userToken = instanceIdResult.getToken();
                                    user_database2 = user_database.child(firebaseUser.getUid());
                                    // users 키에 접근하기 위한 파이어베이스 경로 설정

                                    if (userUid == null) { // 신규 사용자 일 경우
                                        realarm = "o"; mealarm = "o"; noticealarm = "o";
                                        // 알림 여부의 기본값을 설정

                                        User user = new User(firebaseUser.getDisplayName(), firebaseUser.getUid(), loginWay, userToken, realarm, mealarm, noticealarm);
                                        user_database2.setValue(user);
                                        // 이름, uid, 로그인 경로, 알람 기본값, 기기토큰을 담아서 파이어베이스 업로드

                                    } else { // 기존에 존재하는 사용자 일 경우
                                        Map<String, Object> tokenChange = new HashMap<>();
                                        tokenChange.put("userToken", userToken);
                                        user_database2.updateChildren(tokenChange);
                                        // userToken은 변경 될 수 있는 값이므로 변경 되었으면 값을 변경 시키고 로그인 할 수 있도록 함
                                    }
                                    Toast.makeText(MainActivity.this, firebaseUser.getDisplayName() + " 님 환영합니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    // 로그인 성공 했으므로 환영한다는 Toast 메세지와 화면 이동
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}