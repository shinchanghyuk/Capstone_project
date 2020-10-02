package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RelativeBoardAdapter extends RecyclerView.Adapter<RelativeBoardAdapter.ViewHolder> {
    private ArrayList<RelativeBoardItem> arrayList;
    private Context context;

    public RelativeBoardAdapter(ArrayList<RelativeBoardItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.matching.setText(arrayList.get(position).getMatching());
        holder.title.setText(arrayList.get(position).getTitle());
        holder.day.setText(arrayList.get(position).getDay());
        holder.user.setText(arrayList.get(position).getUser());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RelativeBoardContentActivity.class);
                intent.putExtra("boardNum", position);
                Log.d("posi", String.valueOf(position));
                context.startActivity(intent);
            }
        });
    }
    // 테이블 타이틀 디자인
    // 화면 이동 시 게시판 나오게(데이터 베이스 다시 하기 - 제목이랑 사용자 비교 해서 검색 불러오기 )

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView matching;
        TextView title;
        TextView day;
        TextView user;

        ViewHolder(View itemView) {
            super(itemView);

            this.matching = itemView.findViewById(R.id.recycler_item1);
            this.title = itemView.findViewById(R.id.recycler_item2);
            this.day = itemView.findViewById(R.id.recycler_item3);
            this.user = itemView.findViewById(R.id.recycler_item4);
        }
    }
}

