package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RelativeBoardContentActivity extends AppCompatActivity {

    Intent intent;
    int number;
    TextView rb_reigonTv, rb_dateTv, rb_abliTv, rb_numTv, rb_content;
    Button tm_menu_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_board_content);

        rb_abliTv = findViewById(R.id.rb_abliTv);
        rb_dateTv = findViewById(R.id.rb_dateTv);
        rb_reigonTv = findViewById(R.id.rb_regionTv);
        rb_numTv = findViewById(R.id.rb_numTv);
        rb_content = findViewById(R.id.tm_con);

        intent = getIntent();
        number = intent.getIntExtra("number" , -1);

        String[]  num = intent.getStringArrayExtra("num");
        String[]  region = intent.getStringArrayExtra("region");
        String[]  date = intent.getStringArrayExtra("date");
        String[]  cont = intent.getStringArrayExtra("con");
        String[] ability = intent.getStringArrayExtra("ability");

        switch (number)
        {
            case 0:
                rb_numTv.setText(num[0]);
                rb_reigonTv.setText(region[0]);
                rb_dateTv.setText(date[0]);
                rb_abliTv.setText(ability[0]);
                rb_content.setText(cont[0]);
                break;
            case 1:
                rb_numTv.setText(num[1]);
                rb_reigonTv.setText(region[1]);
                rb_dateTv.setText(date[1]);
                rb_abliTv.setText(ability[1]);
                rb_content.setText(cont[1]);
                break;
            case 2:
                rb_numTv.setText(num[2]);
                rb_reigonTv.setText(region[2]);
                rb_dateTv.setText(date[2]);
                rb_abliTv.setText(ability[2]);
                rb_content.setText(cont[2]);
                break;
            case 3:
                rb_numTv.setText(num[3]);
                rb_reigonTv.setText(region[3]);
                rb_dateTv.setText(date[3]);
                rb_abliTv.setText(ability[3]);
                rb_content.setText(cont[3]);
                break;

            case 4:
                rb_numTv.setText(num[4]);
                rb_reigonTv.setText(region[4]);
                rb_dateTv.setText(date[4]);
                rb_abliTv.setText(ability[4]);
                rb_content.setText(cont[4]);
                break;
            case 5:
                rb_numTv.setText(num[5]);
                rb_reigonTv.setText(region[5]);
                rb_dateTv.setText(date[5]);
                rb_abliTv.setText(ability[5]);
                rb_content.setText(cont[5]);
                break;
            case 6:
                rb_numTv.setText(num[6]);
                rb_reigonTv.setText(region[6]);
                rb_dateTv.setText(date[6]);
                rb_abliTv.setText(ability[6]);
                rb_content.setText(cont[6]);
                break;
            case 7:
                rb_numTv.setText(num[7]);
                rb_reigonTv.setText(region[7]);
                rb_dateTv.setText(date[7]);
                rb_abliTv.setText(ability[7]);
                rb_content.setText(cont[7]);
                break;
            case 8:
                rb_numTv.setText(num[8]);
                rb_reigonTv.setText(region[8]);
                rb_dateTv.setText(date[8]);
                rb_abliTv.setText(ability[8]);
                rb_content.setText(cont[8]);
                break;
            case 9:
                rb_numTv.setText(num[9]);
                rb_reigonTv.setText(region[9]);
                rb_dateTv.setText(date[9]);
                rb_abliTv.setText(ability[9]);
                rb_content.setText(cont[9]);
                break;
            case 10:
                rb_numTv.setText(num[10]);
                rb_reigonTv.setText(region[10]);
                rb_dateTv.setText(date[10]);
                rb_abliTv.setText(ability[10]);
                rb_content.setText(cont[10]);
                break;
        }

        tm_menu_btn = findViewById(R.id.tm_btnList);

        tm_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelativeBoardContentActivity.this, RelatvieBoardActivity.class);
                startActivity(intent);
            }
        });
    }

}
