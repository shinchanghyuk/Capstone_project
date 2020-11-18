package com.example.capstone_project.notice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;
import com.example.capstone_project.mypage.Manager;

import java.util.ArrayList;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.ViewHolder> {
    private ArrayList<Manager> arrayList;
    private String boardnumber; // 게시물 번호를 나타내는 문자열 선언
    private Context context;

    public NoticeBoardAdapter(ArrayList<Manager> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.day.setText(arrayList.get(position).getWritetime().substring(5));
        holder.name.setText(arrayList.get(position).getName());
        // 리사이클러뷰 목록들을 구성하고 있는 텍스트 뷰에 값을 넣는 역할을 함

        holder.itemView.setOnClickListener(new View.OnClickListener() { // 리사이클러뷰 목록을 클릭했을 때
            @Override
            public void onClick(View v) {
                boardnumber = arrayList.get(position).getBoardnumber();
                // 해당 게시물번호를 변수에 넣음
                Intent intent = new Intent(v.getContext(), NoticeBoardContentActivity.class);
                intent.putExtra("boardnumber", boardnumber);
                context.startActivity(intent);
                // 해당 게시물 번호를 가지고 NoticeBoardContentActivity로 이동
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, day, name;
        // 리사이클러뷰의 목록들을 담당하고 있는 텍스트 뷰를 선언

        ViewHolder(View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.recycler_item1);
            this.day = itemView.findViewById(R.id.recycler_item2);
            this.name = itemView.findViewById(R.id.recycler_item3);
        }
    }
}