package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.Auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {

    private Button confirm_btn; // 확인 버튼
    private RecyclerView city_recyclerView, gu_recyclerView1, gu_recyclerView2, total_recyclerView;  // 리사이클러뷰 선언
    private RecyclerView.LayoutManager layoutManager1, layoutManager2, layoutManager3, layoutManager4;  // 레이아웃 매니저 선언
    private RecyclerView.Adapter city_adapter, gu_adapter, total_adapter;   // 리사이클러뷰 어댑터 선언
    private ArrayList<PlaceItem> city_arrayList, gu_arrayList1, gu_arrayList2; // 아이템 담을 배열리스트
    private ArrayList<TotalPlaceItem> total_arrayList;
    private PlaceItem placeItem; // 아이템
    private TotalPlaceItem totalPlaceItem; // 아이템
    private ArrayList<String> choiceList; // 선택 된 지역들 담는 배열 리스트
    private ArrayList<Integer> choiceNumber; // 선택 된 지역들 번호를 담는 배열 리스트
    private RecyclerView.ViewHolder holder; // 리사이클러뷰 아이템 뷰에 접근하기 위한 holder
    private List<String> listGu1, listGu2; // 서울 구, 인천 구를 담는 배열리스트
    private int num;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);

        init();

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("region", choiceList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void init() {
        confirm_btn = findViewById(R.id.confirm_btn);
        city_recyclerView = findViewById(R.id.city_recyclerview);
        gu_recyclerView1 = findViewById(R.id.gu_recyclerview1);
        gu_recyclerView2 = findViewById(R.id.gu_recyclerview2);
        total_recyclerView = findViewById(R.id.total_recyclerview);

        city_recyclerView.setHasFixedSize(true);
        gu_recyclerView1.setHasFixedSize(true);
        gu_recyclerView2.setHasFixedSize(true);
        total_recyclerView.setHasFixedSize(true);

        layoutManager1 = new LinearLayoutManager(this);
        layoutManager2 = new LinearLayoutManager(this);
        layoutManager4 = new LinearLayoutManager(this);
        layoutManager3 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        city_recyclerView.setLayoutManager(layoutManager1);
        gu_recyclerView1.setLayoutManager(layoutManager2);
        gu_recyclerView2.setLayoutManager(layoutManager4);
        total_recyclerView.setLayoutManager(layoutManager3);

        city_arrayList = new ArrayList<>();
        gu_arrayList1 = new ArrayList<>();
        gu_arrayList2 = new ArrayList<>();
        total_arrayList = new ArrayList<>();
        choiceList = new ArrayList<>();
        choiceNumber = new ArrayList<>();

        List<String> listCity = Arrays.asList(getResources().getStringArray(R.array.city));
        listGu1 = Arrays.asList(getResources().getStringArray(R.array.seoul_gu));
        listGu2 = Arrays.asList(getResources().getStringArray(R.array.incheon_gu));

        for (int i = 0; i < listCity.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(listCity.get(i));
            city_arrayList.add(placeItem);
        }
        city_adapter = new CityPlaceAdapter(city_arrayList, this, city_recyclerView);
        city_recyclerView.setAdapter(city_adapter);
        city_adapter.notifyDataSetChanged();

        for (int i = 0; i < listGu1.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(listGu1.get(i));
            gu_arrayList1.add(placeItem);
        }

        gu_adapter = new GuPlaceAdapter(gu_arrayList1, this, choiceList.size());
        gu_recyclerView1.setAdapter(gu_adapter);
        gu_adapter.notifyDataSetChanged();

        for (int i = 0; i < listGu2.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(listGu2.get(i));
            gu_arrayList2.add(placeItem);
        }
        gu_adapter = new GuPlaceAdapter(gu_arrayList2, this, choiceList.size());
        gu_recyclerView2.setAdapter(gu_adapter);
        gu_adapter.notifyDataSetChanged();

        gu_recyclerView2.setVisibility(View.INVISIBLE);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(city_recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        city_recyclerView.addItemDecoration(dividerItemDecoration);

    }
    public void cityChange(int position) {
        if (position == 0) {
            holder = (RecyclerView.ViewHolder)
                    city_recyclerView.findViewHolderForAdapterPosition(position + 1);   // 인천

            gu_recyclerView1.setVisibility(View.VISIBLE);
            gu_recyclerView2.setVisibility(View.INVISIBLE);

        } else if (position == 1) {
            holder = (RecyclerView.ViewHolder)
                    city_recyclerView.findViewHolderForAdapterPosition(position - 1);   // 서울

            gu_recyclerView1.setVisibility(View.INVISIBLE);
            gu_recyclerView2.setVisibility(View.VISIBLE);
        }

        num = position;

        holder.itemView.setEnabled(true);
        holder.itemView.setAlpha(1);
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        // 선택안한 아이템 뷰는 활성화
    }
    public void choice(String choiceRegion, int position) {
        total_arrayList.clear();

        if (num == 0) { // 서울 클릭
            holder = (RecyclerView.ViewHolder)
                    gu_recyclerView1.findViewHolderForAdapterPosition(position);
            choiceNumber.add(position);
        } else if (num == 1) { // 인천 클릭
            holder = (RecyclerView.ViewHolder)
                    gu_recyclerView2.findViewHolderForAdapterPosition(position);
            choiceNumber.add(position);
        }

        holder.itemView.setEnabled(false);
        holder.itemView.setAlpha((float) 0.7);
        holder.itemView.setBackgroundColor(Color.parseColor("#f2f2f2"));

        // for (int i = 0; i < 3; i++) { // 데이터가 비어 있을 때
        choiceList.add(String.valueOf(choiceRegion));
        total();
    }

    public void total() {
        for (int i = 0; i < choiceList.size(); i++) { // 데이터가 들어 있을 때
            totalPlaceItem = new TotalPlaceItem();
            totalPlaceItem.setRegion(choiceList.get(i));
            totalPlaceItem.setCancel(R.drawable.ic_cancel_black_24dp);
            total_arrayList.add(totalPlaceItem);

            total_adapter = new TotalPlaceAdapter(total_arrayList, this);
            total_recyclerView.setAdapter(total_adapter);

            total_adapter.notifyDataSetChanged();
        }
    }
    public void cancel(String place, int position) {
        total_arrayList.clear();

        if (String.valueOf(Arrays.asList(listGu1)).contains(place)) {
            holder = (RecyclerView.ViewHolder)
                    gu_recyclerView1.findViewHolderForAdapterPosition(choiceNumber.get(position));
        }

        if (String.valueOf(Arrays.asList(listGu2)).contains(place)) {
            holder = (RecyclerView.ViewHolder)
                    gu_recyclerView2.findViewHolderForAdapterPosition(choiceNumber.get(position));
        }

        holder.itemView.setEnabled(true);
        holder.itemView.setAlpha(1);
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff")); // 선택 취소 된 아이템 뷰 활성화

        choiceList.remove(position);
        choiceNumber.remove(position);
        gu_adapter = new GuPlaceAdapter(choiceList.size()); // size 변경하기 위함
        total();
    }
}