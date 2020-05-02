package com.geekbrains.myweather;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {
    private int messageId = 1000;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Firebase","MSG " +remoteMessage.getNotification().getBody());
        sendNotification(getBaseContext(),remoteMessage.getNotification().getBody());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("Firebase", "Token " + token);  
    }

    private void sendNotification(Context context, String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My Weather")
                .setContentText(msg);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }

}
