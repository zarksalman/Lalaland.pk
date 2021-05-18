package com.lalaland.ecommerce.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.views.activities.SplashActivity;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/*
 * Created by SalmanHameed on 9/19/2019.
 */

public class MyMessagingService extends FirebaseMessagingService {

    public static final String PRIMARY_CHANNEL = "default";
    private NotificationManager manager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage);

    }

    public void showNotification(RemoteMessage remoteMessage) {

        Notification.Builder notificationBuilder = null;

        Intent notificationIntent = new Intent(AppConstants.mContext, SplashActivity.class);
        notificationIntent.putExtra("alarm_service", true);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppConstants.mContext, 0, notificationIntent, FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
                    AppConstants.mContext.getString(R.string.my_app_name), NotificationManager.IMPORTANCE_DEFAULT);
            chan1.setLightColor(R.color.colorPrimary);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(chan1);

            try {
                notificationBuilder = new Notification.Builder(AppConstants.mContext, PRIMARY_CHANNEL)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                getManager().notify(1, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            notificationBuilder = new Notification.Builder(AppConstants.mContext);
            try {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(AppConstants.mContext.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                getManager().notify(1, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) AppConstants.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
