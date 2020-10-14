package com.example.capstone_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private String[] spinnerDate;
    private TextView data_textView;

    public SpinnerAdapter(String[] spinnerDate, Context context) {
        this.spinnerDate = spinnerDate;
        this.context = context;
    }

    public int getCount() {
        return spinnerDate.length;
    }

    @Override
    public Object getItem(int position) {
        return spinnerDate[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner, null);
        }

        data_textView = convertView.findViewById(R.id.data_textView);
        data_textView.setText(spinnerDate[position]);

        return convertView;
    }
}
