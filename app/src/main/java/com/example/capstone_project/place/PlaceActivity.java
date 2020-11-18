package com.example.capstone_project.place;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {

    private Button confirm_btn; // 확인 버튼 선언
    private RecyclerView city_recyclerView, gu_recyclerView1, gu_recyclerView2, total_recyclerView; // 리사이클러뷰 선언
    private RecyclerView.Adapter city_adapter, gu_adapter, total_adapter; // 리사이클러뷰 어댑터 선언
    private ArrayList<PlaceItem> city_arrayList, gu_arrayList1, gu_arrayList2; // 아이템 담을 배열리스트
    private ArrayList<TotalPlaceItem> total_arrayList;
    private PlaceItem placeItem;// placeItem 선언
    private TotalPlaceItem totalPlaceItem; // totalPlaceItem 선언
    private String choicePlace = "x"; // 선택 된 지역담는 문자열
    private int choiceNumber = 0; // 선택 된 지역들 번호를 Integer
    private RecyclerView.ViewHolder holder; // 리사이클러뷰 아이템 뷰에 접근하기 위한 holder
    private List<String> listGu1, listGu2; // 서울 구, 인천 구를 담는 배열리스트
    private int num; // 선택 한 구 위치 담는 변수 선언

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);

        init(); // 미리 설정되어야 하는 것들을 담은 메소드

        // 확인 버튼 눌렀을 때 동작
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choicePlace.equals("x")) {
                    Toast.makeText(getApplicationContext(), "지역을 선택하세요", Toast.LENGTH_SHORT).show();
                    // 선택된 구가 없을 시 Toast 메세지 전송
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("region", choicePlace);
                    setResult(RESULT_OK, intent);
                    finish();
                    // 사용자가 선택한 구를 가지고 이동
                }
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
        // 리사이클러뷰의 성능 향상을 위함

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        city_recyclerView.setLayoutManager(layoutManager1);
        gu_recyclerView1.setLayoutManager(layoutManager2);
        gu_recyclerView2.setLayoutManager(layoutManager3);
        total_recyclerView.setLayoutManager(layoutManager4);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        city_arrayList = new ArrayList<>();
        gu_arrayList1 = new ArrayList<>();
        gu_arrayList2 = new ArrayList<>();
        total_arrayList = new ArrayList<>();
        // 도시, 구, 사용자가 선택한 구의 데이터를 담을 배열리스트 생성

        List<String> listCity = Arrays.asList(getResources().getStringArray(R.array.city));

        listGu1 = Arrays.asList(getResources().getStringArray(R.array.seoul_gu));
        listGu2 = Arrays.asList(getResources().getStringArray(R.array.incheon_gu));

        for (int i = 0; i < listCity.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(listCity.get(i));
            city_arrayList.add(placeItem);
        }
        city_adapter = new CityPlaceAdapter(city_arrayList, this);
        // CityPlaceAdapter 객체를 생성
        city_recyclerView.setAdapter(city_adapter);
        // 리사이클러뷰에 도시 데이터들을 담음

        for (int i = 0; i < listGu1.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(listGu1.get(i));
            gu_arrayList1.add(placeItem);
        } // listGu1에 해당하는 데이터를 배열리스트에 담음

        gu_adapter = new GuPlaceAdapter(gu_arrayList1, this, choiceNumber);
        // GuPlaceAdapter 객체를 생성
        gu_recyclerView1.setAdapter(gu_adapter);
        // 리사이클러뷰에 구 데이터들을 담음
        gu_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

        for (int i = 0; i < listGu2.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(listGu2.get(i));
            gu_arrayList2.add(placeItem);
        } // listGu2에 해당하는 데이터를 배열리스트에 담음

        gu_adapter = new GuPlaceAdapter(gu_arrayList2, this, choiceNumber);
        // GuPlaceAdapter 객체를 생성
        gu_recyclerView2.setAdapter(gu_adapter);
        // 리사이클러뷰에 구 데이터들을 담음
        gu_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

        gu_recyclerView2.setVisibility(View.INVISIBLE);
        // 초기값으로 서울지역의 구를 보이게 함

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(city_recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        city_recyclerView.addItemDecoration(dividerItemDecoration);
        // 리사이클러뷰 목록을 구별하기 위해 밑선을 추가
    }
    // 사용자가 도시를 변경할 때 동작되는 메소드
    public void cityChange(int position) {
        if (position == 0) {
            holder = (RecyclerView.ViewHolder)
                    city_recyclerView.findViewHolderForAdapterPosition(position + 1);
            // 리사이클러뷰 인천 접근

        } else if (position == 1) {
            holder = (RecyclerView.ViewHolder)
                    city_recyclerView.findViewHolderForAdapterPosition(position - 1);
            // 리사이클러뷰 서울 접근
        }
        num = position; // 사용자가 선택한 리사이클러뷰 목록의 위치를 num 벼눗에 넣음

        holder.itemView.setEnabled(true);
        holder.itemView.setAlpha(1);
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        // 선택 안한 리사이클러뷰 목록은 활성화가 됨
    }

    // 사용자가 지역 구를 선택하였을 때 발생하는 메소드
        public void choice(String choiceRegion, int position) {
        if (num == 0) { // 리사이클러뷰 서울 접근
            holder = (RecyclerView.ViewHolder)
                    gu_recyclerView1.findViewHolderForAdapterPosition(position);

        } else if (num == 1) { // 리사이클러뷰 인천 접근
            holder = (RecyclerView.ViewHolder)
                    gu_recyclerView2.findViewHolderForAdapterPosition(position);
        }

        holder.itemView.setEnabled(false);
        holder.itemView.setAlpha((float) 0.7);
        holder.itemView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        // 선택한 리사이클러뷰 목록은 비활성화가 됨

        choicePlace = choiceRegion; // 사용자가 선택한 구를 변수에 넣음
        choiceNumber = position; // 사용자가 선택한 구의 위치를 변수에 넣음
        total(); // total 메소드 호출
    }

    // 사용자가 선택한 구를 띄워주는 역할
    public void total() {
        total_arrayList.clear(); // 배열리스트 초기화

        totalPlaceItem = new TotalPlaceItem();
        totalPlaceItem.setRegion(choicePlace);
        totalPlaceItem.setCancel(R.drawable.ic_cancel_black_24dp);
        // totalPlaceItem에 선택한 지역과 취소 버튼을 넣음
        total_arrayList.add(totalPlaceItem);
        // 배열리스트에 아까 넣은 데이터들을 넣음

        total_adapter = new TotalPlaceAdapter(total_arrayList, this);
        // TotalPlaceAdapter 객체를 생성
        total_recyclerView.setAdapter(total_adapter);
        // 리사이클러뷰에 구 데이터들을 담음
        total_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
    }

    public void cancel(String place) {
        total_arrayList.clear();

        if (String.valueOf(Arrays.asList(listGu1)).contains(place)) {
            holder = (RecyclerView.ViewHolder)
                    gu_recyclerView1.findViewHolderForAdapterPosition(choiceNumber);
        }

        if (String.valueOf(Arrays.asList(listGu2)).contains(place)) {
            holder = (RecyclerView.ViewHolder)
                    gu_recyclerView2.findViewHolderForAdapterPosition(choiceNumber);
        }
        holder.itemView.setEnabled(true);
        holder.itemView.setAlpha(1);
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff")); // 선택 취소 된 아이템 뷰 활성화

        choicePlace = "x";
        choiceNumber = 0;

        gu_adapter = new GuPlaceAdapter(choiceNumber); // size 변경하기 위함
    }
}