package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillId;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private ArrayList<CommentItem> arrayList;
    private String user, current_user, current_uid, commentnum, uid, board;
    private Context context;
    private FirebaseAuth auth;


    public CommentAdapter(ArrayList<CommentItem> arrayList, Context context, String board) {
        this.arrayList = arrayList;
        this.context = context;
        this.board = board;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        user = arrayList.get(position).getUser();
        holder.user.setText(user);
        holder.writetime.setText(arrayList.get(position).getWritetime());
        holder.content.setText(arrayList.get(position).getContent());
        holder.recomcount.setText("답글 " + arrayList.get(position).getRecomcount()+ "개");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentnum = arrayList.get(position).getCommentnum();
                Intent intent = new Intent(v.getContext(), RecommentActivity.class);
                intent.putExtra("commentnum", commentnum);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                uid = arrayList.get(position).getUid();
                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                current_uid = firebaseUser.getUid();
                commentnum = arrayList.get(position).getCommentnum();

                if (current_uid.equals(uid)){
                    ConfirmDialog dialog = new ConfirmDialog(context, commentnum);
                    if(board.equals("Realtive")) {
                        dialog.operation("comment", "Realtive");
                    } else if (board.equals("Mercenary")) {
                        dialog.operation("comment", "Mercenary");
                    } else {
                        dialog.operation("comment", "Team");
                    }
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