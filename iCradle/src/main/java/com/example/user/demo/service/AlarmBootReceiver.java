package com.example.user.demo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.user.demo.database.DatabaseHelper;
import com.example.user.demo.models.DateItem;

/**
 * Created by User on 04.02.2016.
 */
public class AlarmBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notification = new NotificationHelper();
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        DateItem dateItem = databaseHelper.getDateItem(id);

        if (dateItem.getDateEnable() == 1) {
            notification.sendBasicNotification(context, dateItem);
        } else {
            SendAlarm alarm = new SendAlarm();
            alarm.cancelAlarm(context, dateItem.getId());
            alarm.cancelNotification(context, dateItem.getId());
        }
        Toast.makeText(context, "Ukaaa", Toast.LENGTH_SHORT).show();

        context.startService(new Intent(context, TaskButlerService.class));

    }
}
