package com.example.capstone_project.stadium;

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

import com.example.capstone_project.R;
import com.example.capstone_project.mercenary.MercenaryBoardContentActivity;
import com.example.capstone_project.mypage.MypageItem;
import com.example.capstone_project.place.PlaceActivity;
import com.example.capstone_project.relative.RelativeBoardActivity;
import com.example.capstone_project.relative.RelativeBoardContentActivity;
import com.example.capstone_project.relative.RelativeWritingActivity;
import com.example.capstone_project.report.ReportHistoryAdapter;
import com.example.capstone_project.report.ReportItem;
import com.example.capstone_project.team.TeamBoardContentActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class StadiumAdapter extends RecyclerView.Adapter<StadiumAdapter.ViewHolder>{
    private ArrayList<StadiumItem> arrayList;
    private Context context;
    private String stadium_name; // 사용자가 리사이클러뷰 목록에서 선택한 경기장 이름

    public StadiumAdapter(ArrayList<StadiumItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stadium_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.name.setText(arrayList.get(position).getName());
        // 리사이클러뷰 목록들을 구성하고 있는 텍스트 뷰에 값을 넣는 역할을 함

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stadium_name = arrayList.get(position).getName();
                // 사용자가 리사이클러뷰 목록에 선택한 경기장 이름을 변수에 담음

                ((StadiumSelectActivity) context).map(stadium_name);
                // 경기장 이름과 같이 StadiumDetailsActivity로 이동
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        // 리사이클러뷰의 목록들을 담당하고 있는 텍스트 뷰 선언

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.stadium_recycler_item);
        }
    }
}
