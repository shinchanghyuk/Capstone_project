package com.example.capstone_project;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class ConfirmDialog {
    private Context context;
    private Button okButton, cancelButton;
    private TextView content, title;

    public ConfirmDialog(Context context) {
        this.context = context;
    }

    public void operation(final String standard) {
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

        if(standard.equals("matching1")) {
            title.setText("매칭완료 알림");
            content.setText("매칭을 완료 하셨습니까?");
        } else if(standard.equals("matching2")) {
            title.setText("매칭취소 알림");
            content.setText("매칭을 취소 하겠습니까?");
        }else if(standard.equals("delete")) {
            title.setText("게시물삭제 알림");
            content.setText("게시글을 삭제 하시겠습니까?");
        } else if(standard.equals("update")) {
            title.setText("게시물수정 알림");
            content.setText("게시글을 수정 하시겠습니까?");
        }

        dialog.show();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(standard.equals("matching1") || standard.equals("matching2")) {
                    ((RelativeBoardContentActivity) context).matchingChange();
                } else if(standard.equals("delete")) {
                    ((RelativeBoardContentActivity) context).boardDelete();
                }else if(standard.equals("update")) {
                    ((RelativeBoardContentActivity) context).boardUpdate();
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
