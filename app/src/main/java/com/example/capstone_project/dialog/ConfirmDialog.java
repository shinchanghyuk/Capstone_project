package com.example.capstone_project.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.capstone_project.MenuActivity;
import com.example.capstone_project.R;
import com.example.capstone_project.comment.RecommentActivity;
import com.example.capstone_project.mercenary.MercenaryBoardContentActivity;
import com.example.capstone_project.mypage.MypageActivity;
import com.example.capstone_project.notice.NoticeBoardContentActivity;
import com.example.capstone_project.relative.RelativeBoardContentActivity;
import com.example.capstone_project.team.TeamBoardContentActivity;

public class ConfirmDialog {
    private Context context;
    private Button okButton, cancelButton; // 버튼들 선언
    private TextView content, title; // 다이어로그의 제목, 내용을 담을 텍스트뷰 선언
    private String commentnum; // 댓글을 삭제할 때 필요한 문자열 선언

    public ConfirmDialog(Context context) {
        this.context = context;
    }

    public ConfirmDialog(Context context, String commentnum) {
        this.context = context;
        this.commentnum = commentnum;
    }

    public void operation(final String standard, final String activity) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirm_dialog);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        // 다이어로그의 위치와 크기를 조정하기 위한 코드

        okButton = (Button) dialog.findViewById(R.id.okButton);
        cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        content = dialog.findViewById(R.id.dialog_content);
        title = dialog.findViewById(R.id.dialog_title);

        if (standard.equals("matching1")) {
            // 사용자가 게시물이 매칭완료가 되었는지 확인 할 때
            title.setText("매칭완료 알림");
            content.setText("매칭을 완료 하셨습니까?");
        } else if (standard.equals("matching2")) {
            // 사용자가 게시물이 매칭취소가 되었는지 확인 할 때
            title.setText("매칭취소 알림");
            content.setText("매칭을 취소 하겠습니까?");
        } else if (standard.equals("delete")) {
            // 사용자가 게시물을 삭제 할 것인지 확인 할 때
            title.setText("게시물삭제 알림");
            content.setText("게시글을 삭제 하시겠습니까?");
        } else if (standard.equals("update")) {
            // 사용자가 게시물을 수정할 것인지 확인 할 때
            title.setText("게시물수정 알림");
            content.setText("게시글을 수정 하시겠습니까?");
        } else if (standard.equals("logout")) {
            // 사용자가 로그아웃을 할 것인지 확인 할 때
            title.setText("로그아웃 알림");
            content.setText("로그아웃을 하시겠습니까?");
        } else if (standard.equals("withdrawal")) {
            // 사용자가 회원탈퇴를 할 것인지 확인할 때
            title.setText("회원탈퇴 알림");
            content.setText("회원탈퇴를 하시겠습니까?");
        } else if (standard.equals("comment")) {
            // 사용자가 댓글을 삭제 할 것인지 확인 할 때
            title.setText("댓글삭제 알림");
            content.setText("댓글을 삭제 하시겠습니까?");
        } else if (standard.equals("process")) {
            // 관리자가 신고된 게시물 또는 댓글을 조치하였는지 확인 할 때
            title.setText("조치완료 알림");
            content.setText("신고된 게시물을 조치하셨습니까?");
        }   // 다이얼로그의 제목과 내용의 텍스트 뷰를 설정하는 코드

            dialog.show(); // 다이얼로그를 보여줌

            // 다이얼로그에서 확인버튼을 눌렀을 때 동작
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (standard.equals("matching1") || standard.equals("matching2")) {
                        // 매칭완료 또는 매칭 취소에 관련있는 알림일 때
                        if (activity.equals("relative")) { // 상대매칭 게시판 일때
                            ((RelativeBoardContentActivity) context).matchingChange();
                            // context를 이용하여 matchingChange 메소드 호출
                        } else if (activity.equals("mercenary")) { // 용병모집 게시판 일때
                            ((MercenaryBoardContentActivity) context).matchingChange();
                            // context를 이용하여 matchingChange 메소드 호출
                        }

                    } else if (standard.equals("delete")) {
                        // 게시물 삭제와 관련있는 알림일 때
                        if (activity.equals("relative")) { // 상대매칭 게시판 일 때
                            ((RelativeBoardContentActivity) context).boardDelete();
                            // context를 이용해 boardDelete 메소드 호출
                        } else if (activity.equals("team")) { // 팀 홍보 게시판 일 때
                            ((TeamBoardContentActivity) context).boardDelete();
                            // context를 이용해 boardDelete 메소드 호출
                        } else if (activity.equals("mercenary")) { // 용병모집 게시판 일 때
                            ((MercenaryBoardContentActivity) context).boardDelete();
                            // context를 이용해 boardDelete 메소드 호출
                        } else if (activity.equals("notice")) { // 공지사항 게시판 일 때
                            ((NoticeBoardContentActivity) context).boardDelete();
                            // context를 이용해 boardDelete 메소드 호출
                        }

                    } else if (standard.equals("update")) {
                        // 게시물 수정과 관련있는 알림일 때
                        if (activity.equals("relative")) { // 상대매칭 게시판 일 때
                            ((RelativeBoardContentActivity) context).boardUpdate();
                            // context를 이용해 boardUpdate 메소드 호출
                        } else if (activity.equals("team")) { // 팀 홍보 게시판 일 때
                            ((TeamBoardContentActivity) context).boardUpdate();
                            // context를 이용해 boardUpdate 메소드 호출
                        } else if (activity.equals("mercenary")) { // 용병모집 게시판 일 때
                            ((MercenaryBoardContentActivity) context).boardUpdate();
                            // context를 이용해 boardUpdate 메소드 호출
                        } else if (activity.equals("notice")) { // 공지사항 게시판 일 때
                            ((NoticeBoardContentActivity) context).boardUpdate();
                            // context를 이용해 boardUpdate 메소드 호출
                        }

                    } else if (standard.equals("logout")) {
                        // 로그아웃과 관련있는 알람일 때
                        if (activity.equals("mypage")) { // 마이페이지에서 리사이클러뷰 목록에 로그아웃을 눌렀을 때
                            ((MypageActivity) context).logout();
                            // context를 이용해 logout 메소드 호출
                        } else if (activity.equals("menu")) { // 메뉴화면에서 로그아웃 버튼을 눌렀을 때
                            ((MenuActivity) context).logout();
                            // context를 이용해 logout 메소드 호출
                        }
                    } else if (standard.equals("withdrawal")) {
                        // 회원탈퇴와 관련있는 알람일 때
                        if (activity.equals("mypage")) { // 마이페이지에서 리사이클러뷰 목록에 회원탈퇴를 눌렀을 때
                            ((MypageActivity) context).userWithdrawal();
                            // context를 이용해 userWithdrawal 메소드 호출
                        }
                    } else if (standard.equals("comment")) {
                        // 댓글삭제와 관련있는 알람일 때
                        if (activity.equals("relative")) { // 상대매칭 게시판에 있는 댓글 일 때
                            ((RelativeBoardContentActivity) context).commentDelete(commentnum);
                            // context를 이용해 commentDelete 메소드 호출(commentnum을 가지고)
                        } else if (activity.equals("mercenary")) { // 용병모집 게시판에 있는 댓글 일 때
                            ((MercenaryBoardContentActivity) context).commentDelete(commentnum);
                            // context를 이용해 commentDelete 메소드 호출(commentnum을 가지고)
                        } else if (activity.equals("team")) { // 팀 홍보 게시판에 있는 댓글 일 때
                            ((TeamBoardContentActivity) context).commentDelete(commentnum);
                            // context를 이용해 commentDelete 메소드 호출(commentnum을 가지고)
                        } else if (activity.equals("notice")) { // 공지사항 게시판에 있는 댓글 일 때
                            ((NoticeBoardContentActivity) context).commentDelete(commentnum);
                            // context를 이용해 commentDelete 메소드 호출(commentnum을 가지고)
                        } else if (activity.equals("recomment")) { // 대댓글 삭제와 관련있는 알람일 때
                            ((RecommentActivity) context).commentDelete(commentnum);
                            // context를 이용해 commentDelete 메소드 호출(commentnum을 가지고)
                        }
                    } else if (standard.equals("process")) {
                        // 관리자의 조치완료와 관련있는 알람일 때
                        if (activity.equals("relative")) { // 상대매칭 게시판의 게시물일 때
                            ((RelativeBoardContentActivity) context).process();
                            // context를 이용해 process 메소드 호출
                        } else if (activity.equals("mercenary")) { // 용병모집 게시판의 게시물일 때
                            ((MercenaryBoardContentActivity) context).process();
                            // context를 이용해 process 메소드 호출
                        } else if (activity.equals("team")) { // 팀 홍보 게시판의 게시물일 때
                            ((TeamBoardContentActivity) context).process();
                            // context를 이용해 process 메소드 호출
                        }
                    }
                    dialog.dismiss();
                    // 동작을 마친 후 다이얼로그를 끔
                }
            });

            // 다이얼로그에서 취소버튼을 눌렀을 때 동작
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    // 다이얼로그를 끔
                }
            });

        }
    }
