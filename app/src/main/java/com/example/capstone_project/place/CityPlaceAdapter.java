package com.example.capstone_project.place;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityPlaceAdapter extends RecyclerView.Adapter<CityPlaceAdapter.ViewHolder> {
    private ArrayList<PlaceItem> city_arrayList; // placeItem 아이템
    private Context context;

    public CityPlaceAdapter(ArrayList<PlaceItem> arrayList, Context context) {
        this.city_arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_recycler_item2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.city.setText(city_arrayList.get(position).getRegion());
        // 리사이클러뷰 목록들을 구성하고 있는 텍스트 뷰에 값을 넣는 역할을 함

        holder.itemView.setOnClickListener(new View.OnClickListener() { // 리사이클러뷰 목록을 클릭했을 때
            @Override
            public void onClick(View v) {
                holder.itemView.setEnabled(false);
                // 사용자가 선택한 목록은 비활성화를 만듬

                ((PlaceActivity) context).cityChange(position);
                // 사용자가 선택한 지역의 위치를 가지고 PlaceActivity로 이동
            }
        });
    }
    @Override
    public int getItemCount() {
        return city_arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView city;
        // 리사이클러뷰의 목록들을 담당하고 있는 텍스트 뷰를 선언

        ViewHolder(View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.place_recycler_item);
        }
    }
}


