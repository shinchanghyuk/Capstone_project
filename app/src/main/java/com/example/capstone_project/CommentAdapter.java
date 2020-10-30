package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private ArrayList<CommentItem> arrayList;
    private String commentnumber;
    private Context context;

    public CommentAdapter(ArrayList<CommentItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relative_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.user.setText(arrayList.get(position).getUser());
        holder.writetime.setText(arrayList.get(position).getWritetime());
        holder.content.setText(arrayList.get(position).getContent());
        holder.recomcount.setText("답글 " + arrayList.get(position).getRecomcount()+ "개");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 대댓글 작동시 연결 시킬 부분
            }
        });
    }
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        TextView writetime;
        TextView content;
        TextView recomcount;

        ViewHolder(View itemView) {
            super(itemView);

            this.user = itemView.findViewById(R.id.replyName);
            this.writetime = itemView.findViewById(R.id.replyTime);
            this.recomcount = itemView.findViewById(R.id.rreplyCount);
            this.content = itemView.findViewById(R.id.replyTxt);
        }
    }

}




