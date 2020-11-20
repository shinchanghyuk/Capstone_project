package com.example.capstone_project.stadium;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.capstone_project.R;
import com.example.capstone_project.mypage.MypageItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StadiumInfoAdapter extends RecyclerView.Adapter<StadiumInfoAdapter.ViewHolder>{
    private List<String> stadiumImageList;

    public StadiumInfoAdapter(List<String> stadiumImageList) {
        this.stadiumImageList = stadiumImageList;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stadium_image_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String stadium_image = stadiumImageList.get(position);
        // 여러개의 경기장 이미지 url를 하나하나씩 변수에 넣음

            Glide.with(holder.itemView.getContext())
                    .load(stadium_image).error(R.drawable.img_error).into(holder.image);
            // Glide를 이용하여 이미지 url을 리사이클러뷰에 띄움
    }

    @Override
    public int getItemCount() {
        return stadiumImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        // 리사이클러뷰의 목록들을 담당하고 있는 텍스트 뷰와 이미지 뷰를 선언

        ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.stadium_image);
        }
    }
}

