package com.example.user.demo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.user.demo.models.DateItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by User on 06.02.2016.
 */
public class SendAlarm {


    public void cancelAlarm(Context context, int id) {
        PendingIntent pendingIntent = getPendingIntent(context, id);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public void cancelNotification(Context context, int id) {
        NotificationHelper cancel = new NotificationHelper();
        cancel.cancelNotification(context, id);
    }

    public void setAlarm(Context context, DateItem dateItem) {
        Calendar calSet = Calendar.getInstance();
        for (String str : dateItem.getSelectedDays()) {
            if (str.equals("Monday")) {
                setTime(calSet, Calendar.MONDAY, dateItem, context);
            } else if (str.equals("Tuesday")) {
                setTime(calSet, Calendar.TUESDAY, dateItem, context);
            } else if (str.equals("Wednesday")) {
                setTime(calSet, Calendar.WEDNESDAY, dateItem, context);
            } else if (str.equals("Thursday")) {
                setTime(calSet, Calendar.THURSDAY, dateItem, context);
            } else if (str.equals("Friday")) {
                setTime(calSet, Calendar.FRIDAY, dateItem, context);
            } else if (str.equals("Saturday")) {
                setTime(calSet, Calendar.SATURDAY, dateItem, context);
            } else if (str.equals("Sunday")) {
                setTime(calSet, Calendar.SUNDAY, dateItem, context);
            }
        }
    }

    private void setTime(Calendar calSet, int dayOfWeek, DateItem dateItem, Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        calSet.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calSet.set(Calendar.HOUR_OF_DAY, dateItem.getHour());
        calSet.setTimeInMillis(System.currentTimeMillis());
        calSet.set(Calendar.MINUTE, dateItem.getMinutes());
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7,
                getPendingIntent(context, dateItem.getId()));
    }


    private long parseDate(String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(Calendar.getInstance().getTimeZone());
            return sdf.parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    PendingIntent getPendingIntent(Context context, int id) {
        Intent intent = new Intent(context, AlarmBootReceiver.class)
                .putExtra("id", id);
        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

////    public DateItem setRepeatingAlarm(Context context, int id){
////        DatabaseHelper db = DatabaseHelper.getInstance(context);
////        DateItem task = db.getDateItem(id);
////        Calendar newDateDue = (Calendar) task.getDateDueCal().clone();
////        int repeatType;
////
////        switch(task.getRepeatType()){
////            case Task.MINUTES:
////                repeatType = Calendar.MINUTE;
////                break;
////            case Task.HOURS:
////                repeatType = Calendar.HOUR_OF_DAY;
////                break;
////            case Task.DAYS:
////                repeatType = Calendar.DAY_OF_YEAR;
////                break;
////            case Task.WEEKS:
////                repeatType = Calendar.WEEK_OF_YEAR;
////                break;
////            case Task.MONTHS:
////                repeatType = Calendar.MONTH;
////                break;
////            case Task.YEARS:
////                repeatType = Calendar.YEAR;
////                break;
////            default:
////                repeatType = Calendar.DAY_OF_YEAR;
////                break;
////        }
//
//        // Due date is behind current time, task was finished late
//        if (newDateDue.getTimeInMillis() <= System.currentTimeMillis()) {
//            while(newDateDue.getTimeInMillis() <= System.currentTimeMillis()){
//                newDateDue.add(repeatType, task.getRepeatInterval());
//            }
//        } else {
//            // Due date is ahead of current time, task was finished early
//            newDateDue.add(repeatType, task.getRepeatInterval());
//
//            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//            am.set(AlarmManager.RTC_WAKEUP, newDateDue.getTimeInMillis(),
//                    getPendingIntent(context, task.getID()));
//
//            // Return task unchanged, it will uncomplete automatically when
//            // TaskAlarmService when current time >= date due
//            return task;
//        }
//
//        task.setDateDue(newDateDue.getTimeInMillis());
//        task.setDateModified(System.currentTimeMillis());
//        task.setIsCompleted(false);
//        db.updateTask(task);
//
//        return task;
//    }
}
