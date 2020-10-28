package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TotalPlaceAdapter extends RecyclerView.Adapter<TotalPlaceAdapter.ViewHolder> {
    private ArrayList<TotalPlaceItem> arrayList;
    private Context context;

    public TotalPlaceAdapter(ArrayList<TotalPlaceItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_recycler_item1, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.totalPlace.setText(arrayList.get(position).getRegion());
        holder.cancelImage.setImageResource(arrayList.get(position).getCancel());

        holder.cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String total = arrayList.get(position).getRegion();
                ((PlaceActivity) context).cancel(total);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView totalPlace;
        private ImageView cancelImage;

        ViewHolder(View itemView) {
            super(itemView);

            totalPlace = itemView.findViewById(R.id.place_recycler_item);
            cancelImage = itemView.findViewById(R.id.cancel_Image);
        }
    }
}

