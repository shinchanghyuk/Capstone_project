package com.example.capstone_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class recommentAdapter extends RecyclerView.Adapter<recommentAdapter.ViewHolder> {

    private ArrayList<recommentItem> arrayList;
    private String recommentnum, current_user, User, recomnum;
    private Context context;
    private FirebaseAuth auth;

    public recommentAdapter(ArrayList<recommentItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recomment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;

    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        User = arrayList.get(position).getUser();
        holder.user.setText(User);
        holder.writetime.setText(arrayList.get(position).getWritetime());
        holder.content.setText(arrayList.get(position).getContent());
        recommentnum = arrayList.get(position).getRecommentnum();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                current_user = firebaseUser.getDisplayName();
                recomnum = arrayList.get(position).getRecommentnum();
                if (current_user.equals(User)) {
                    ConfirmDialog confirmDialog = new ConfirmDialog(context, recomnum);
                    confirmDialog.operation("comment", "recomment");


                } else {
                    // 현 유저와 작성자가 다를시 작동 구문 ( 신고 )

                }
                return false;
            }

        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        TextView writetime;
        TextView content;

        ViewHolder(View itemView) {
            super(itemView);

            this.user = itemView.findViewById(R.id.recommentUser);
            this.writetime = itemView.findViewById(R.id.recommentTime);
            this.content = itemView.findViewById(R.id.recommentTxt);
        }
    }
}