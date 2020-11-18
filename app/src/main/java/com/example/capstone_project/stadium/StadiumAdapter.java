package com.example.capstone_project.stadium;

import android.content.Context;
import android.content.Intent;
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
import com.example.capstone_project.relative.RelativeBoardContentActivity;
import com.example.capstone_project.report.ReportHistoryAdapter;
import com.example.capstone_project.report.ReportItem;
import com.example.capstone_project.team.TeamBoardContentActivity;

import java.util.ArrayList;

public class StadiumAdapter extends RecyclerView.Adapter<StadiumAdapter.ViewHolder>{
    private ArrayList<MypageItem> arrayList;
    private Context context;
    private Intent intent;

    public StadiumAdapter(ArrayList<MypageItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(arrayList.get(position).getTitle());
        holder.image.setImageResource(arrayList.get(position).getMoveImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView image;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.mypage_recycler_item);
            image = itemView.findViewById(R.id.move_Image);
        }
    }
}
