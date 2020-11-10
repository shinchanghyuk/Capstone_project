package com.example.capstone_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

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

            sendNotification(messageTitle, messageBody);
        }
    }

    private void sendNotification(String messageTitle, String messageBody){

        if (messageTitle.equals("상대매칭 게시판 알림") || messageTitle.equals("상대매칭 게시판 댓글알림")) {
            intent = new Intent(this, RelativeBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else if (messageTitle.equals("용병모집 게시판 알림") || messageTitle.equals("용병모집 게시판 댓글알림")) {
            intent = new Intent(this, MercenaryBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // Notification 채널을 설정합니다.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "notification";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}