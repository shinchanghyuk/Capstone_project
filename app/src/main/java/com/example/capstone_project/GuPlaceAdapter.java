package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GuPlaceAdapter extends RecyclerView.Adapter<GuPlaceAdapter.ViewHolder> {
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private ArrayList<PlaceItem> arrayList;
    private Context context;
    private static int size;

    public GuPlaceAdapter(ArrayList<PlaceItem> arrayList, Context context, int size) {
        this.arrayList = arrayList;
        this.context = context;
        this.size = size;
    }
    public GuPlaceAdapter(int size) {
        this.size = size;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_recycler_item2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.gu.setText(arrayList.get(position).getRegion());

        if (holder.isItemSelected(position)) {
            holder.itemView.setEnabled(false);
            holder.itemView.setAlpha((float) 0.7);
            holder.itemView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            holder.itemView.setEnabled(true);
            holder.itemView.setAlpha(1);
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (size < 1) {
                    holder.toggleItemSelected(position); // 선택 된 position만 계속 선택되게 함
                    String choiceRegion = arrayList.get(position).getRegion();
                    size++;
                    ((PlaceActivity) context).choice(choiceRegion, position);
                } else {
                    Toast.makeText(v.getContext(), "이미 지역을 선택하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView gu;

        ViewHolder(View itemView) {
            super(itemView);

            gu = itemView.findViewById(R.id.place_recycler_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    toggleItemSelected(position);
                }
            });
        }
        private void toggleItemSelected(int position) {
            if (mSelectedItems.get(position, false) == true) {
                mSelectedItems.delete(position);
                notifyItemChanged(position);
            } else {
                mSelectedItems.put(position, true);
                notifyItemChanged(position);
            }
        }
        private boolean isItemSelected(int position) {
            return mSelectedItems.get(position, false);
        }
    }
}


