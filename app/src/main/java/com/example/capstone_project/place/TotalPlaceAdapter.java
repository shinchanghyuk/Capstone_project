package com.example.capstone_project.place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;

import java.util.ArrayList;

public class TotalPlaceAdapter extends RecyclerView.Adapter<TotalPlaceAdapter.ViewHolder> {
    private ArrayList<TotalPlaceItem> arrayList; // TotalplaceItem 아이템
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
        // 리사이클러뷰 목록들을 구성하고 있는 텍스트 뷰와 이미지 뷰에 값을 넣는 역할을 함

        holder.cancelImage.setOnClickListener(new View.OnClickListener() { // 리사이클러뷰 목록을 클릭했을 때
            @Override
            public void onClick(View v) {
                String total = arrayList.get(position).getRegion();
                ((PlaceActivity) context).cancel();
                // 사용자가 선택했던 지역과 PlaceActivitiy의 cancel 메소드로 이동

                Toast.makeText(v.getContext(), total + " 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                // 사용자가 지역을 선택했으므로 Toast 메세지 전송
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
        // 리사이클러뷰의 목록들을 담당하고 있는 텍스트 뷰와 이미지 뷰를 선언

        ViewHolder(View itemView) {
            super(itemView);

            totalPlace = itemView.findViewById(R.id.place_recycler_item);
            cancelImage = itemView.findViewById(R.id.cancel_Image);
        }
    }
}

