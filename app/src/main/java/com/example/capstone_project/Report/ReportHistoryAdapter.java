package com.example.capstone_project.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;
import com.example.capstone_project.mercenary.MercenaryBoardContentActivity;
import com.example.capstone_project.relative.RelativeBoardContentActivity;
import com.example.capstone_project.team.TeamBoardContentActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReportHistoryAdapter extends RecyclerView.Adapter<ReportHistoryAdapter.ViewHolder> {
    private ArrayList<ReportItem> arrayList;
    private String boardnumber, board;
    private Context context;
    private Intent intent;

    public ReportHistoryAdapter(ArrayList<ReportItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        board = arrayList.get(position).getBoardtype();
        // 게시판의 종류를 변수에 담음
        holder.board.setText(board);
        holder.type.setText(arrayList.get(position).getType());
        holder.check.setText(arrayList.get(position).getCheck() + "로 의심되는 신고");
        holder.writetime.setText(arrayList.get(position).getWritetime().substring(5,10));
        holder.user.setText(arrayList.get(position).getUser());
        // 리사이클러뷰 목록들을 구성하고 있는 텍스트 뷰에 값을 넣는 역할을 함

        holder.itemView.setOnClickListener(new View.OnClickListener() { // 리사이클러뷰 목록을 클릭했을 때
            @Override
            public void onClick(View v) {
                boardnumber = arrayList.get(position).getBoardnumber();
                // 해당 게시물번호를 변수에 넣음
                if (board.equals("상대매칭")) {
                    intent = new Intent(v.getContext(), RelativeBoardContentActivity.class);
                    // board 변수가 상대매칭일 때 상대매칭 게시판의 글임
                } else if (board.equals("용병모집")) {
                    intent = new Intent(v.getContext(), MercenaryBoardContentActivity.class);
                    // board 변수가 용병모집일 때 용병모집 게시판의 글임
                } else if (board.equals("팀 홍보")) {
                    intent = new Intent(v.getContext(), TeamBoardContentActivity.class);
                    // board 변수가 팀 홍보일 때 팀 홍보 게시판의 글임
                }
                intent.putExtra("boardnumber", boardnumber);
                context.startActivity(intent);
                // 해당 게시물 번호를 가지고 각 액티비티로 이동
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView board, check, type, writetime, user;
        // 리사이클러뷰의 목록들을 담당하고 있는 텍스트 뷰들 선언

        ViewHolder(View itemView) {
            super(itemView);

            this.board = itemView.findViewById(R.id.recycler_item1);
            this.check = itemView.findViewById(R.id.recycler_item2);
            this.writetime = itemView.findViewById(R.id.recycler_item3);
            this.user = itemView.findViewById(R.id.recycler_item4);
            this.type = itemView.findViewById(R.id.recycler_item5);
        }
    }
}