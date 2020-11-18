package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000);
        // 3초 후에 hd handler 실행

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation);
        animationView.setAnimation("soccerplayer.json");
        animationView.loop(true);
        animationView.playAnimation();
        // 애니메이션 동작
    }

    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class));
            //로딩이 끝난 후 MainActivity 이동
            SplashActivity.this.finish();
            // 로딩페이지 Activity stack에서 제거됨
        }
}
}
