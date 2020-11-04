package com.example.capstone_project;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RelativeAlarmActivity extends AppCompatActivity {
    private TextView place_textView;
    private RadioGroup ability_radio;
    private RadioButton radio1, radio2, radio3;
    private Button place_btn, confirm_btn;
    private Spinner startTime, endTime;
    private EditText person_edit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_matching_alarm);
        init();
    }

    private void init() {
        place_textView = findViewById(R.id.place_textView);
        ability_radio = findViewById(R.id.ability_radio);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        person_edit = findViewById(R.id.person_edit);
        place_btn = findViewById(R.id.place_btn);
        confirm_btn = findViewById(R.id.confirm_btn);
        ability_radio = findViewById(R.id.ability_radio);
        radio1.findViewById(R.id.radio1);
        radio2.findViewById(R.id.radio2);
        radio3.findViewById(R.id.radio3);
    }
}
