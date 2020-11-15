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
        // 1초 후에 hd handler 실행  3000ms = 3초

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation);
        animationView.setAnimation("soccerplayer.json");
        animationView.loop(true);
        animationView.playAnimation();
    }

    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class));
            //로딩이 끝난 후, ChoiceFunction 이동
            SplashActivity.this.finish();
            // 로딩페이지 Activity stack에서 제거
        }
    }
}
