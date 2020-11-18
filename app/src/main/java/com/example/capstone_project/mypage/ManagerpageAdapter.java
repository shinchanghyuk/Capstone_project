package com.example.capstone_project.mypage;

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
import com.example.capstone_project.dialog.ConfirmDialog;
import com.example.capstone_project.notice.NoticeBoardActivity;
import com.example.capstone_project.report.ReportHistoryActivity;

import java.util.ArrayList;

public class ManagerpageAdapter extends RecyclerView.Adapter<ManagerpageAdapter.ViewHolder> {
        private ArrayList<MypageItem> arrayList;
        private Context context;
        private ConfirmDialog dialog;

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
            // 리사이클러뷰 목록들을 구성하고 있는 텍스트 뷰와 이미지 뷰에 값을 넣는 역할을 함

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 리사이클러뷰 목록을 클릭했을 때
                    switch (position) {
                        case 0:
                            Intent intent = new Intent(v.getContext(), ReportHistoryActivity.class);
                            context.startActivity(intent);
                            // 신고내역 화면으로 이동
                            break;
                        case 1:
                            Intent intent2 = new Intent(v.getContext(), NoticeBoardActivity.class);
                            context.startActivity(intent2);
                            // 공지사항 게시판 화면으로 이동
                            break;
                        case 2:
                            dialog = new ConfirmDialog(v.getContext());
                            dialog.operation("logout", "mypage");
                            // 로그아웃 다이어로그를 띄움
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
            // 리사이클러뷰의 목록들을 담당하고 있는 텍스트 뷰와 이미지 뷰를 선언

            ViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.mypage_recycler_item);
                moveImage = itemView.findViewById(R.id.move_Image);
            }
        }
    }