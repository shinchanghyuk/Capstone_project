package com.example.capstone_project;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog {
    private Context context;
    private Button okButton, cancelButton;
    private TextView content, title;
    private String commentnum;

    public ConfirmDialog(Context context) {
        this.context = context;
    }

    public ConfirmDialog(Context context, String commentnum) {
        this.context = context;
        this.commentnum = commentnum;
    }

    public void operation(final String standard, final String activity) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        okButton = (Button) dialog.findViewById(R.id.okButton);
        cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        content = dialog.findViewById(R.id.dialog_content);
        title = dialog.findViewById(R.id.dialog_title);

        if (standard.equals("matching1")) {
            title.setText("매칭완료 알림");
            content.setText("매칭을 완료 하셨습니까?");
        } else if (standard.equals("matching2")) {
            title.setText("매칭취소 알림");
            content.setText("매칭을 취소 하겠습니까?");
        } else if (standard.equals("delete")) {
            title.setText("게시물삭제 알림");
            content.setText("게시글을 삭제 하시겠습니까?");
        } else if (standard.equals("update")) {
            title.setText("게시물수정 알림");
            content.setText("게시글을 수정 하시겠습니까?");
        } else if (standard.equals("logout")) {
            title.setText("로그아웃 알림");
            content.setText("로그아웃을 하시겠습니까?");
        } else if (standard.equals("withdrawal")) {
            title.setText("회원탈퇴 알림");
            content.setText("회원탈퇴를 하시겠습니까?");
        } else if (standard.equals("comment")) {
            title.setText("댓글삭제 알림");
            content.setText("댓글을 삭제 하시겠습니까?");
        }

            dialog.show();
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (standard.equals("matching1") || standard.equals("matching2")) {
                        if(activity.equals("relative")) {
                            ((RelativeBoardContentActivity) context).matchingChange();
                        } else if(activity.equals("mercenary")) {
                            ((MercenaryBoardContentActivity) context).matchingChange();
                        }
                    } else if (standard.equals("delete")) {
                        if(activity.equals("relative")) {
                            ((RelativeBoardContentActivity) context).boardDelete();
                        } else if(activity.equals("team")) {
                            ((TeamBoardContentActivity) context).boardDelete();
                        } else if(activity.equals("mercenary")) {
                            ((MercenaryBoardContentActivity) context).boardDelete();
                        }
                    } else if (standard.equals("update")) {
                        if(activity.equals("relative")) {
                            ((RelativeBoardContentActivity) context).boardUpdate();
                        } else if(activity.equals("team")) {
                            ((TeamBoardContentActivity) context).boardUpdate();
                        } else if(activity.equals("mercenary")) {
                            ((MercenaryBoardContentActivity) context).boardUpdate();
                        }
                    } else if (standard.equals("logout")) {
                        ((MypageActivity) context).logout();
                    } else if (standard.equals("withdrawal")) {
                        ((MypageActivity) context).withdrawal();
                    }else if (standard.equals("comment")){
                        if(activity.equals("comment")) {
                            ((RelativeBoardContentActivity) context).commentDelete(commentnum);
                        }else if (activity.equals("recomment")){
                            ((recommentActivity) context).commentDelete(commentnum);
                        }
                    }
                    dialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                }
            });

        }
    }
