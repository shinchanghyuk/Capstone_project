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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private ArrayList<CommentItem> arrayList;
    private String user, writetime, content, recomcount,current_user , commentnum;
    private Context context;
    private FirebaseAuth auth;

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

        user = arrayList.get(position).getUser();
        writetime = arrayList.get(position).getWritetime();
        content = arrayList.get(position).getContent();
        recomcount = "답글 " + arrayList.get(position).getRecomcount()+ "개";

        holder.user.setText(user);
        holder.writetime.setText(writetime);
        holder.content.setText(content);
        holder.recomcount.setText(recomcount);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentnum = arrayList.get(position).getCommentnum();
                Intent intent = new Intent(v.getContext(), recommentActivity.class);

                intent.putExtra("commentnum", commentnum);
                context.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                current_user = firebaseUser.getDisplayName();
                commentnum = arrayList.get(position).getCommentnum();
                if (current_user.equals(user)){
                    ConfirmDialog dialog = new ConfirmDialog(context, commentnum);
                    dialog.operation("comment", "comment");

                } else {
                    // 사용자와 댓글 작성자가 다를시 작동구문 (신고)
                }


                return false;
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