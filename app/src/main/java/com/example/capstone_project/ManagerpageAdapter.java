package com.example.capstone_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManagerpageAdapter extends RecyclerView.Adapter<ManagerpageAdapter.ViewHolder> {
        private ArrayList<MypageItem> arrayList;
        private Context context;
        private ConfirmDialog dialog1, dialog2;

    public ManagerpageAdapter(ArrayList<MypageItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

        @Override
        public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_recycler_item, parent, false);
           ViewHolder holder = new ManagerpageAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder ( @NonNull final ManagerpageAdapter.ViewHolder holder, final int position){
            holder.title.setText(arrayList.get(position).getTitle());
            holder.moveImage.setImageResource(arrayList.get(position).getMoveImage());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                        //    Intent intent = new Intent(v.getContext(), AlarmSettingActivity.class);
                      //      context.startActivity(intent);
                            break;
                        case 1:
                            Intent intent2 = new Intent(v.getContext(), ManagerWritingActivity.class);
                            context.startActivity(intent2);
                            break;
                        case 2:
                            dialog1 = new ConfirmDialog(v.getContext());
                            dialog1.operation("logout", "manager");
                            break;
                        case 3:
                            dialog2 = new ConfirmDialog(v.getContext());
                            dialog2.operation("withdrawal", null);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount () {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private ImageView moveImage;

            ViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.mypage_recycler_item);
                moveImage = itemView.findViewById(R.id.move_Image);
            }
        }
    }