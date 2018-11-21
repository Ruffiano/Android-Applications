package com.example.user.demo.service;

import android.content.Intent;

import com.example.user.demo.database.DatabaseHelper;
import com.example.user.demo.models.DateItem;

import java.util.List;

/**
 * Created by User on 06.02.2016.
 */
public class AlarmHandle extends WakefulIntentService {
    public AlarmHandle(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SendAlarm sendAlarm = new SendAlarm();

        DatabaseHelper db = DatabaseHelper.getInstance(this); //get access to the instance of TasksDataSource

        List<DateItem> dateItems = db.getAllDates();
        for (DateItem dateItem : dateItems) {

            sendAlarm.setAlarm(this, dateItem);


        }
        super.onHandleIntent(intent);

    }
}
