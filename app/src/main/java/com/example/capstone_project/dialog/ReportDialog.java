package com.example.capstone_project.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.capstone_project.R;
import com.example.capstone_project.mercenary.MercenaryBoardContentActivity;
import com.example.capstone_project.relative.RelativeBoardContentActivity;
import com.example.capstone_project.team.TeamBoardContentActivity;

public class ReportDialog {
    private Context context;
    private Button okButton, cancelButton; // 버튼들 선언
    private TextView title; // 다이어로그의 제목을 담을 텍스트뷰 선언
    private RadioGroup type_radio; // 신고유형 라디오 버튼 그룹 선언
    private EditText content; // 기타사유를 적을 수 있는 edit 선언
    private String commentnum, total_type="", total_content; // 쓰이는 문자열 선언

    public ReportDialog(Context context) {
        this.context = context;
    }

    public ReportDialog(Context context, String commentnum) {
        this.context = context;
        this.commentnum = commentnum;
    }

    public void operation(final String type, final String activity) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.report_dialog);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        // 다이어로그의 위치와 크기를 조정하기 위한 코드

        okButton = (Button) dialog.findViewById(R.id.okButton);
        cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        title = dialog.findViewById(R.id.dialog_title);
        type_radio = dialog.findViewById(R.id.type_radio);
        content = dialog.findViewById(R.id.content_edit);

        type_radio.clearCheck(); // 만약 선택된 라디오 버튼이 있을 시 초기화

        title.setText("신고 알림"); // 다이얼로그 제목 텍스트 설정
        dialog.show(); // 다이얼로그를 보여줌

        // 신고유형 라디오 버튼을 선택하였을 때 동작
        type_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = dialog.findViewById(checkedId);
                // 선택된 라디오 버튼을 선언 및 생성
                total_type = radioButton.getText().toString();
                // 선택된 라디오 버튼의 값을 변수에 담음
            }
        });

        // 다이얼로그의 확인 버튼을 눌렀을 때 동작
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (total_type.isEmpty()) { // 사용자가 신고유형을 선택하지 않았을 때
                    Toast.makeText(context.getApplicationContext(), "신고 유형을 선택하세요.", Toast.LENGTH_SHORT).show(); // Toast 메세지 전송
                } else {
                    total_content = content.getText().toString(); // 사용자가 작성한 기타 사유를 변수에 담음
                    if (activity.equals("relative")) { // 상대매칭 게시판의 게시물일 때
                            ((RelativeBoardContentActivity) context).report(type, "상대매칭", total_type, total_content);
                            // context를 이용해서 report 메소드 호출(신고유형, 기타사유, 어디 게시판에서 일어났는지, 게시판인지 댓글인지 나타내는 변수와 같이)
                        } else if(activity.equals("mercenary")) { // 용병모집 게시판의 게시물일 때
                            ((MercenaryBoardContentActivity) context).report(type, "용병모집", total_type, total_content);
                        // context를 이용해서 report 메소드 호출(신고유형, 기타사유, 어디 게시판에서 일어났는지, 게시판인지 댓글인지 나타내는 변수와 같이)
                    } else if(activity.equals("team")) { // 팀 홍보 게시판의 게시물일 때
                        ((TeamBoardContentActivity) context).report(type, "팀 홍보", total_type, total_content);
                        // context를 이용해서 report 메소드 호출(신고유형, 기타사유, 어디 게시판에서 일어났는지, 게시판인지 댓글인지 나타내는 변수와 같이)
                    }
                    dialog.dismiss();
                    // 동작을 마친 후 다이얼로그를 끔
                }
            }
        });

        // 다이얼로그의 취소 버튼을 눌렀을 때 동작
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                // 다이얼로그를 끔
            }
        });
    }
}





