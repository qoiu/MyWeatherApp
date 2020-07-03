package com.geekbrains.myweather;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.geekbrains.myweather.model.AppSettings;


public class EventReceiver extends BroadcastReceiver {
    private int messageId = 1000;


    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = "";
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") &&
                AppSettings.get().isInternet() && !isConnected(context)) {

            sendNotification(context,"Warning: Internet connection failed");
        }
        AppSettings.get().setInternet(isConnected(context));
        if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
            sendNotification(context,"Warning: Battery low");
        }
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

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < 28) {
            assert cm != null;
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) return true;
                return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
            return false;
        } else {
                if (cm != null) {
                    NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            return true;
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            return true;
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                            return true;
                        }
                    }
                }
            return false;
        }

    }


}
