package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CityPlaceAdapter extends RecyclerView.Adapter<CityPlaceAdapter.ViewHolder> {
    private ArrayList<PlaceItem> arrayList;
    private Context context;

    public CityPlaceAdapter(ArrayList<PlaceItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_recycler_item2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.city.setText(arrayList.get(position).getRegion());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setEnabled(false);

                ((PlaceActivity) context).cityChange(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView city;

        ViewHolder(View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.place_recycler_item);
        }
    }
}


