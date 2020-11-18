package com.example.capstone_project.comment;


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
import com.example.capstone_project.dialog.ConfirmDialog;
import com.example.capstone_project.dialog.ReportDialog;
import com.example.capstone_project.mypage.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<CommentItem> arrayList;
    private String current_uid, uid, commentnum, board, manager_uid=""; // 쓰이는 문자열 선언
    private Context context;
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private FirebaseDatabase firebaseDatabase; // 파이어베이스 데이터베이스 객체 선언
    private DatabaseReference manager_database; // 파이버에시스 연결(경로) 선언

    public CommentAdapter(ArrayList<CommentItem> arrayList, Context context, String board) {
        this.arrayList = arrayList;
        this.context = context;
        this.board = board;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.user.setText(arrayList.get(position).getUser());
        holder.writetime.setText(arrayList.get(position).getWritetime());
        holder.content.setText(arrayList.get(position).getContent());
        holder.recomcount.setText("답글 " + arrayList.get(position).getRecomcount() + "개");
        // 댓글을 구성하고 있는 텍스트 뷰에 값을 넣는 역할을 함

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            // 댓글 리사이클러뷰를 클릭했을 때
            @Override
            public void onClick(View v) {
                commentnum = arrayList.get(position).getCommentnum();
                // 그 댓글의 번호를 가져와 변수에 담음
                Intent intent = new Intent(v.getContext(), RecommentActivity.class);
                intent.putExtra("commentnum", commentnum);
                context.startActivity(intent);
                // 해당 댓글 번호를 가지고 RecommentActivity로 이동
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            // 댓글 리사이클러뷰를 롱클릭 했을 때
            @Override
            public boolean onLongClick(View view) {
                uid = arrayList.get(position).getUid(); // 롱 클릭한 댓글을 작성한 사용자의 uid를 변수에 담음
                firebaseDatabase = FirebaseDatabase.getInstance();
                // 파이어베이스 데이터베이스 객체 생성
                auth = FirebaseAuth.getInstance();
                // 파이어베이스 인증 객체 생성
                FirebaseUser firebaseUser = auth.getCurrentUser();
                // 인증 객체를 통해서 현재 접속한 유저의 정보를 얻을 수 있는 파이어베이스유저 객체 생성
                current_uid = firebaseUser.getUid();
                // 현재 접속한 사용자의 uid와 이름을 가져옴
                commentnum = arrayList.get(position).getCommentnum();
                // 해당 댓글의 번호를 가져와 변수에 담음

                manager_database = firebaseDatabase.getReference("manager");
                // manager 키에 접근하기 위한 파이어베이스 경로 설정
                Query query = manager_database.orderByChild("uid").equalTo(current_uid);
                // manager 키에 있는 uid와 현재 사용자의 uid를 비교

                query.addListenerForSingleValueEvent(new ValueEventListener() { // manager 키에 일치하는 uid가 있다면
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Manager managerItem = snapshot.getValue(Manager.class);
                            manager_uid = managerItem.getUid();
                            // 일치하는 사용자의 uid를 가져옴
                        }

                        if (!(manager_uid.isEmpty()) || current_uid.equals(uid)) {
                            // 이 사용자가 관리자 또는 댓글을 작성한 사용자 일 때 -> 댓글을 지울 수 있는 것은 관리자 또는 댓글을 작성한 사용자이다.
                            ConfirmDialog dialog = new ConfirmDialog(context, commentnum);
                            if (board.equals("relative")) { // 상대매칭 게시판에서 일어났을 때
                                dialog.operation("comment", "relative");
                                // 댓글을 삭제할 것인지 나타내는 다이얼로그를 띄움
                            } else if (board.equals("mercenary")) { // 용병모집 게시판에서 일어났을 때
                                dialog.operation("comment", "mercenary");
                                // 댓글을 삭제할 것인지 나타내는 다이얼로그를 띄움
                            } else if (board.equals("team")){ // 팀 홍보 게시판에서 일어났을 때
                                dialog.operation("comment", "team");
                                // 댓글을 삭제할 것인지 나타내는 다이얼로그를 띄움
                            } else if (board.equals("notice")) { // 공지사항 게시판에서 일어났을 때
                                dialog.operation("comment", "notice");
                                // 댓글을 삭제할 것인지 나타내는 다이얼로그를 띄움
                            }
                        } else { // 관리자 또는 댓글을 작성한 사용자가 아닐 때 -> 다른 일반 사용자 일 때
                            ReportDialog dialog = new ReportDialog(context, commentnum);
                            if (board.equals("relative")) { // 상대매칭 게시판에서 일어났을 때
                                dialog.operation("댓    글", "relative");
                                // 신고접수의 기능을 가진 다이얼로그를 띄움
                            } else if (board.equals("mercenary")) { // 용병모집 게시판에서 일어났을 때
                                dialog.operation("댓    글", "mercenary");
                                // 신고접수의 기능을 가진 다이얼로그를 띄움
                            } else if(board.equals("team")){ // 팀 홍보 게시판에서 일어났을 때
                                dialog.operation("댓    글", "team");
                                // 신고접수의 기능을 가진 다이얼로그를 띄움
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return false;
            }
        });
    }

        public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user, writetime, content, recomcount;
        // 댓글을 구성하고 있는 텍스트 뷰들 선언

        ViewHolder(View itemView) {
            super(itemView);

            this.user = itemView.findViewById(R.id.replyName);
            this.writetime = itemView.findViewById(R.id.replyTime);
            this.recomcount = itemView.findViewById(R.id.rreplyCount);
            this.content = itemView.findViewById(R.id.replyTxt);
        }
    }
}