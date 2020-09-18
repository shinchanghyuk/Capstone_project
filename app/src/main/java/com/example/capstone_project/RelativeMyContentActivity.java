package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RelativeMyContentActivity extends AppCompatActivity {
    private int number;
    private Intent intent;
    private TextView mrb_reigonTv, mrb_dateTv, mrb_abliTv, mrb_numTv, mrb_content;
    private Button[]  my_tm_menu_btn;
    private String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_myboard_content);

        intent = getIntent();

        mrb_abliTv = findViewById(R.id.mrb_abliTv);
        mrb_dateTv = findViewById(R.id.mrb_dateTv);
        mrb_reigonTv = findViewById(R.id.mrb_regionTv);
        mrb_numTv = findViewById(R.id.mrb_numTv);
        mrb_content = findViewById(R.id.my_tm_con);

        String[] title = intent.getStringArrayExtra("title");
        String[] num = intent.getStringArrayExtra("num");
        String[] region = intent.getStringArrayExtra("region");
        String[] cont = intent.getStringArrayExtra("con");
        String[] date = intent.getStringArrayExtra("date");
        String[] ability = intent.getStringArrayExtra("ability");
        name = intent.getStringExtra("name");
        number = intent.getIntExtra("number", -1);

        switch (number)
        {
            case 0:
                mrb_numTv.setText(num[0]);
                mrb_reigonTv.setText(region[0]);
                mrb_dateTv.setText(date[0]);
                mrb_abliTv.setText(ability[0]);
                mrb_content.setText(cont[0]);
                break;
            case 1:
                mrb_numTv.setText(num[1]);
                mrb_reigonTv.setText(region[1]);
                mrb_dateTv.setText(date[1]);
                mrb_abliTv.setText(ability[1]);
                mrb_content.setText(cont[1]);
                break;
            case 2:
                mrb_numTv.setText(num[2]);
                mrb_reigonTv.setText(region[2]);
                mrb_dateTv.setText(date[2]);
                mrb_abliTv.setText(ability[2]);
                mrb_content.setText(cont[2]);
                break;
            case 3:
                mrb_numTv.setText(num[3]);
                mrb_reigonTv.setText(region[3]);
                mrb_dateTv.setText(date[3]);
                mrb_abliTv.setText(ability[3]);
                mrb_content.setText(cont[3]);
                break;

            case 4:
                mrb_numTv.setText(num[4]);
                mrb_reigonTv.setText(region[4]);
                mrb_dateTv.setText(date[4]);
                mrb_abliTv.setText(ability[4]);
                mrb_content.setText(cont[4]);
                break;
            case 5:
                mrb_numTv.setText(num[5]);
                mrb_reigonTv.setText(region[5]);
                mrb_dateTv.setText(date[5]);
                mrb_abliTv.setText(ability[5]);
                mrb_content.setText(cont[5]);
                break;
        }

        my_tm_menu_btn = new Button[5];

        my_tm_menu_btn[4] = findViewById(R.id.my_tm_btnList);

        my_tm_menu_btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelativeMyContentActivity.this, RelativeMyBoardActivity.class);
                startActivity(intent);
            }
        });
    }
}
