package com.example.capstone_project.place;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuPlaceAdapter extends RecyclerView.Adapter<GuPlaceAdapter.ViewHolder> {
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    // position 별 선택상태를 저장하는 구조
    private ArrayList<PlaceItem> arrayList; // placeItem 아이템 선언
    private Context context;
    private static int size; // 사용자가 선택한 지역의 수

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
        // 리사이클러뷰 목록들을 구성하고 있는 텍스트 뷰에 값을 넣는 역할을 함

        if (holder.isItemSelected(position)) {
            holder.itemView.setEnabled(false);
            holder.itemView.setAlpha((float) 0.7);
            holder.itemView.setBackgroundColor(Color.parseColor("#f2f2f2"));
            // 사용자가 선택한 지역의 위치에 있는 리사이클러뷰 목록은 비 활성화
        } else {
            holder.itemView.setEnabled(true);
            holder.itemView.setAlpha(1);
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        } // 사용자가 선택한 지역의 위치가 아닌 리사이클러뷰 목록들은 활성화

        holder.itemView.setOnClickListener(new View.OnClickListener() { // 리사이클러뷰 목록을 클릭했을 때
            public void onClick(View v) {
                if (size < 1) { // 사용자가 선택한 지역이 없을 때
                    holder.toggleItemSelected(position);
                    // 사용자가 선택한 position만 계속 선택되게 함
                    String choiceRegion = arrayList.get(position).getRegion();
                    size++; // 사용자가 지역을 선택했으므로 증가시킴
                    ((PlaceActivity) context).choice(choiceRegion, position);
                    // 선택한 지역과 그 지역의 위치를 가지고 placeActivity로 이동
                } else {
                    Toast.makeText(v.getContext(), "이미 지역을 선택하였습니다.", Toast.LENGTH_SHORT).show();
                    // 사용자가 지역을 선택했으므로 Toast 메세지 전송
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gu;
        // 리사이클러뷰의 목록들을 담당하고 있는 텍스트 뷰를 선언

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

        public void clearSelectedItem() {
            int position;

            for (int i = 0; i < mSelectedItems.size(); i++) {
                position = mSelectedItems.keyAt(i);
                mSelectedItems.put(position, false);
                notifyItemChanged(position);
            }

            mSelectedItems.clear();
        }
    }
}
