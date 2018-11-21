package com.example.user.demo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.user.demo.R;
import com.example.user.demo.activity.DateDetailActivity;
import com.example.user.demo.models.DateItem;

/**
 * Created by User on 06.02.2016.
 */
public class NotificationHelper {

    public void sendBasicNotification(Context context, DateItem dateItem) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);


        remoteViews.setTextViewText(R.id.time, dateItem.getHour() + " : " + dateItem.getMinutes());
        remoteViews.setTextViewText(R.id.memoText, dateItem.getMemoText());

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Alarm")
                .setContentText(dateItem.getMemoText())
                .setContentIntent(getPendingIntent(context, dateItem.getId()))
//                .setContent(remoteViews)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getNotificationManager(context);
        notificationManager.notify(dateItem.getId(), notification);
    }

    NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    PendingIntent getPendingIntent(Context context, int id) {
        Intent intent = new Intent(context, DateDetailActivity.class);
        intent.putExtra("dateID", id);
        intent.putExtra(DateItem.DATE_EXTRA_ID, DateItem.DATE_FROM_PUSH);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, id, intent, 0);
    }

    public void cancelNotification(Context context, int taskID) {
        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.cancel(taskID);
    }

}
