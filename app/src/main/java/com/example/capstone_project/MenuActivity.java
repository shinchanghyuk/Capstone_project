package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    Button menu_btn[];
    String username;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_select);
        setTitle("종목선택");

        menu_btn = new Button[6];

        Integer btn_id[] = {R.id.Relative_btn, R.id.Mercenary_btn, R.id.Stadium_btn,
                R.id.Promotion_btn, R.id.Declaration_btn, R.id.Preferences_btn};

        for (int i = 0; i < menu_btn.length; i++) {
            menu_btn[i] = findViewById(btn_id[i]);
        }

        Intent intent = getIntent();
        username = intent.getExtras().getString("name");

        menu_btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RelatvieBoardActivity.class);
                intent.putExtra("name", username);
                startActivity(intent);
            }
        });

        menu_btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });

        menu_btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //DB관리
        menu_btn[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, DBmanage.class);
                startActivity(intent);
            }
        });
    }
}