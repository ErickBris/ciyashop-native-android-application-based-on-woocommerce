package com.example.ciyashop.fcm;

/**
 * Created by User on 06-12-2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.ciyashop.R;
import com.example.ciyashop.activity.HomeActivity;
import com.example.ciyashop.activity.MyOrderActivity;
import com.example.ciyashop.activity.RewardsActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    // [START receive_message]

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();

        Log.e("data ", data.toString());
        sendNotification(data);
    }

    private void sendNotification(Map<String, String> data) {
        Bitmap icon;
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Intent intent = null;
        if(data.size()>0) {
            if (Integer.parseInt(data.get("not_code")) == 1) {
                intent = new Intent(this, RewardsActivity.class);
            } else if (Integer.parseInt(data.get("not_code")) == 2) {
                intent = new Intent(this, MyOrderActivity.class);
            } else if (Integer.parseInt(data.get("not_code")) == 3) {
                intent = new Intent(this, HomeActivity.class);
            }
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "");
        notificationBuilder.setContentTitle(data.get("title"));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentInfo("");
        notificationBuilder.setLargeIcon(icon);
        notificationBuilder.setColor(Color.GRAY);
        notificationBuilder.setSmallIcon(R.drawable.logo);
        notificationBuilder.setContentText(data.get("message"));
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }
}
