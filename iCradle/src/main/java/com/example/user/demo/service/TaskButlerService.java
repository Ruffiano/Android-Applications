/*
 * TasksBatlerService.java
 * 
 * Copyright 2012 Jonathan Hasenzahl, James Celona, Dhimitraq Jorgji
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.user.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.user.demo.database.DatabaseHelper;
import com.example.user.demo.models.DateItem;

import java.util.List;


public class TaskButlerService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        SendAlarm alarm = new SendAlarm();


        List<DateItem> tasks = db.getAllDates();
        for (DateItem dateItem : tasks) {
            if (dateItem.getDateEnable() == 0)
                alarm.cancelAlarm(this, dateItem.getId());
            alarm.setAlarm(this, dateItem);

        }
        return START_STICKY;
    }
}