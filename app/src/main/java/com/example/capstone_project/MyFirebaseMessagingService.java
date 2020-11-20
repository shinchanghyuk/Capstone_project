package com.example.capstone_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.capstone_project.mercenary.MercenaryBoardActivity;
import com.example.capstone_project.notice.NoticeBoardActivity;
import com.example.capstone_project.relative.RelativeBoardActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Intent intent;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onNewToken(String token) {  // 새로운 토큰을 확인했을 때 호출
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {    // 메세지를 새롭게 받을 때
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived 호출" + remoteMessage);

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            String messageTitle = remoteMessage.getNotification().getTitle();
            String messageBody = remoteMessage.getNotification().getBody();
            // 알림 제목과 메세지 내용을 변수에 담음

            sendNotification(messageTitle, messageBody);
            // sendNotification 메소드 호출
        }
    }

    private void sendNotification(String messageTitle, String messageBody){

        if (messageTitle.equals("상대매칭 게시판 알림") || messageTitle.equals("상대매칭 게시판 댓글알림")) {
            intent = new Intent(this, RelativeBoardActivity.class);
            // 상대매칭 게시판 일 때
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Activity가 스택의 맨 위에 존재 하는 경우에 기존 Activity를 재활용
        } else if (messageTitle.equals("용병모집 게시판 알림") || messageTitle.equals("용병모집 게시판 댓글알림")) {
            intent = new Intent(this, MercenaryBoardActivity.class);
            // 용병모집 게시판 일 때
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Activity가 스택의 맨 위에 존재 하는 경우에 기존 Activity를 재활용
        } else if(messageTitle.equals("공지사항 알림")) {
            intent = new Intent(this, NoticeBoardActivity.class);
            // 공지사항 게시판 일 때
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Activity가 스택의 맨 위에 존재 하는 경우에 기존 Activity를 재활용
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);
        // pendingIntent를 통해 notification을 클릭하면 각 알림의 종류에 따른 액티비티 실행

        String channelId = getString(R.string.default_notification_channel_id); // 채널 id를 변수에 넣음
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_soccer_icon_foreground);
        // LargeIcon을 사용하기 위해 bitmap 변경

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_soccer_icon_foreground) // 작은 아이콘 설정
                .setColor(ContextCompat.getColor(this, R.color.title))
                .setContentTitle(messageTitle) // 알림 제목
                .setContentText(messageBody) // 알림 메세지
                .setAutoCancel(true) // 알람 터치시 자동으로 삭제
                .setLargeIcon(largeIcon) // 큰 아이콘 설정
                .setSound(defaultSoundUri) // 알림 소리
                .setContentIntent(pendingIntent); // 알람을 눌렀을 때 실행할 인텐트 설정

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // Notification 채널을 설정

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //OREO API 26 이상에서는 채널 필요
            String channelName = "notification";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            // 알림 채널 생성
        }

        notificationManager.notify(0, notificationBuilder.build());
        // 고유숫자로 노티피케이션 동작시킴
    }
}