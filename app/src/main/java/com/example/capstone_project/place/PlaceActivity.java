package com.example.capstone_project.place;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
    private RecyclerView city_recyclerView, gu_recyclerView, total_recyclerView; // 리사이클러뷰 선언
    private RecyclerView.Adapter city_adapter, gu_adapter, total_adapter; // 리사이클러뷰 어댑터 선언
    private ArrayList<PlaceItem> city_arrayList, gu_arrayList; // 아이템 담을 배열리스트
    private ArrayList<TotalPlaceItem> total_arrayList;
    private PlaceItem placeItem; // placeItem 선언
    private TotalPlaceItem totalPlaceItem; // totalPlaceItem 선언
    private String choicePlace = "x"; // 선택 된 지역담는 문자열
    private int choiceNumber = 0; // 선택 된 지역들 번호를 담는 int
    private RecyclerView.ViewHolder holder; // 리사이클러뷰 아이템 뷰에 접근하기 위한 holder
    private List<String> listGu; // 서울 구, 인천 구를 담는 배열리스트
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
        gu_recyclerView = findViewById(R.id.gu_recyclerview);
        total_recyclerView = findViewById(R.id.total_recyclerview);

        city_recyclerView.setHasFixedSize(true);
        gu_recyclerView.setHasFixedSize(true);
        total_recyclerView.setHasFixedSize(true);
        // 리사이클러뷰의 성능 향상을 위함

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        city_recyclerView.setLayoutManager(layoutManager1);
        gu_recyclerView.setLayoutManager(layoutManager2);
        total_recyclerView.setLayoutManager(layoutManager3);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정

        city_arrayList = new ArrayList<>();
        gu_arrayList = new ArrayList<>();
        total_arrayList = new ArrayList<>();
        // 도시, 구, 사용자가 선택한 구의 데이터를 담을 배열리스트 생성

        List<String> listCity = Arrays.asList(getResources().getStringArray(R.array.city));

        for (int i = 0; i < listCity.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(listCity.get(i));
            city_arrayList.add(placeItem);
            // listCity에 해당하는 데이터를 배열리스트에 담음
        }

        city_adapter = new CityPlaceAdapter(city_arrayList, this);
        // CityPlaceAdapter 객체를 생성
        city_recyclerView.setAdapter(city_adapter);
        // 리사이클러뷰에 도시 데이터들을 담음

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(city_recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        city_recyclerView.addItemDecoration(dividerItemDecoration);
        // 리사이클러뷰 목록을 구별하기 위해 밑선을 추가
    }
    // 사용자가 도시를 변경할 때 동작되는 메소드
    public void cityChange(int position) {
        if (position == 0) { // 리사이클러뷰 서울 접근
            holder = (RecyclerView.ViewHolder)
                    city_recyclerView.findViewHolderForAdapterPosition(position + 1);
            listGu = Arrays.asList(getResources().getStringArray(R.array.seoul_gu));
            // 사용자가 리사이클러뷰 목록 중 서울에 접근 하였을 때 listGu에 서울지역의 구 데이터가 들어감
        } else if (position == 1) { // 리사이클러뷰 인천 접근
            holder = (RecyclerView.ViewHolder)
                    city_recyclerView.findViewHolderForAdapterPosition(position - 1);
            listGu = Arrays.asList(getResources().getStringArray(R.array.incheon_gu));
            // 사용자가 리사이클러뷰 목록 중 서울에 접근 하였을 때 listGu에 인천지역의 구 데이터가 들어감
        }

        holder.itemView.setEnabled(true);
        // 선택 안한 리사이클러뷰 목록은 활성화가 됨

        gu_arrayList.clear(); // 지역 구 데이터를 담는 배열리스트 초기화

        for (int i = 0; i < listGu.size(); i++) {
            placeItem = new PlaceItem();
            placeItem.setRegion(listGu.get(i));
            gu_arrayList.add(placeItem);
        } // listGu에 해당하는 데이터를 배열리스트에 담음

        gu_adapter = new GuPlaceAdapter(gu_arrayList, this, choiceNumber);
        // GuPlaceAdapter 객체를 생성
        gu_recyclerView.setAdapter(gu_adapter);
        // 리사이클러뷰에 구 데이터들을 담음
        gu_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

        num = position; // 사용자가 선택한 리사이클러뷰 목록의 위치를 num 변수에 넣음
    }

    // 사용자가 지역 구를 선택하였을 때 발생하는 메소드
    public void choice(String choiceRegion, int position) {

        choicePlace = choiceRegion; // 사용자가 선택한 구를 변수에 넣음
        choiceNumber = position; // 사용자가 선택한 구의 위치를 변수에 넣음
        total(); // total 메소드 호출
    }

    // 사용자가 선택한 구를 띄워주는 역할
    public void total() {
     total_arrayList.clear(); // 선택한 총 지역 구 데이터를 담는 배열리스트 초기화 / 이미 지역이 선택 된 후 다시 지역을 선택하려고 할 때 초기화

        if (!(choicePlace.equals("x"))) { // 선택된 구 지역이 있을 때
            totalPlaceItem = new TotalPlaceItem();
            totalPlaceItem.setRegion(choicePlace);
            totalPlaceItem.setCancel(R.drawable.ic_cancel_black_24dp);
            // totalPlaceItem에 선택한 지역과 취소 버튼을 넣음
            total_arrayList.add(totalPlaceItem);
            // 배열리스트에 아까 넣은 데이터들을 넣음
        }

            total_adapter = new TotalPlaceAdapter(total_arrayList, this);
            // TotalPlaceAdapter 객체를 생성
            total_recyclerView.setAdapter(total_adapter);
            // 리사이클러뷰에 구 데이터들을 담음
            total_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
        }

    // 사용자가 선택한 구 지역 데이터를 없애길 원할 때 동작
    public void cancel() {
        choicePlace = "x"; // 선택한 구 지역 초기화
        choiceNumber = 0; // 선택한 구 지역 수도 0으로 수 변경

        gu_adapter = new GuPlaceAdapter(choiceNumber); // size 변경하기 위함
        gu_adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

        total();
        // total 메소드 호출
    }
}