package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RelativeWritingActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    Button btnPlace, btnDate;
    Button btnUp;
    TextView txtPlace,txtDate;
    EditText etTitle, playerNum, etCon;
    Spinner ability;
    public String pla = "";
    public static Context mContext;

    public void incomA(String str) {
        txtPlace.setText(str);
    }

    String date;

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_writing);

        mContext = this;

        txtPlace = findViewById(R.id.txtPlace);
        System.out.println(pla);
        txtPlace.setText(pla);

        btnDate =  findViewById(R.id.btnDate);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RelativeWritingActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText et_time = findViewById(R.id.Time);
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RelativeWritingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "오전";

                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "오후";
                        }  // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        et_time.setText(state + " " + selectedHour + "시 " + selectedMinute + "분");
                        // EditText에 출력할 형식 지정
                        et_time.setTextSize(10);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        final EditText et_time2 = findViewById(R.id.Time2);
        et_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RelativeWritingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "오전";
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "오후";
                        } // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        et_time2.setText(state + " " + selectedHour + "시 " + selectedMinute + "분");
                        // EditText에 출력할 형식 지정
                        et_time2.setTextSize(10);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btnPlace = findViewById(R.id.btnPlace);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaceActivity.class);
                intent.putExtra( "class","A");
                startActivity(intent);
            }
        });

        btnUp = findViewById(R.id.btnUp);
        txtDate = findViewById(R.id.txtDate);
        etTitle =findViewById(R.id.edtTitle);
        playerNum = findViewById(R.id.spin2);
        ability = findViewById(R.id.spin);
        etCon = findViewById(R.id.edtMain);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("region", txtPlace.getText().toString());
                intent.putExtra("date", date);
                intent.putExtra("title", etTitle.getText().toString());
                intent.putExtra("ability", ability.getSelectedItem().toString());
                intent.putExtra("num", playerNum.getText().toString());
                intent.putExtra("con", etCon.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
    });
    }
    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        String myFormat2 = "M/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.KOREA);

        TextView txtDate = findViewById(R.id.txtDate);

        txtDate.setText(sdf2.format(myCalendar.getTime()));
        date = txtDate.getText().toString();

        txtDate.setText(sdf.format(myCalendar.getTime()));
    }
}