package com.example.user.demo.event;

import com.example.user.demo.models.DateItem;

import java.util.ArrayList;

/**
 * Created by User on 03.02.2016.
 */
public class DateItemEvent {

    private int id;
    private String time;
    private int hour;
    private int minutes;
    private String memoText;
    private String timePosition;
    private int dateEnabled;
    private ArrayList<String> selectedDays;
    private Type type;
    private DateItem dateItem;


    public enum Type {
        DATE_ADDED,
        DATE_CHANGED,
        DATE_NO_CHANGED
    }

    public static DateItemEvent dateItemChanged(int id,int hour, int minutes, String memoText, ArrayList<String> selectedDays) {
        DateItemEvent dateItemEvent = new DateItemEvent();
        dateItemEvent.id = id;
        dateItemEvent.type = Type.DATE_CHANGED;
        dateItemEvent.hour = hour;
        dateItemEvent.minutes = minutes;
        dateItemEvent.memoText = memoText;
        dateItemEvent.selectedDays = selectedDays;
        return dateItemEvent;
    }

    public static DateItemEvent dateItemMoChanged(String time, String memoText, ArrayList<String> selectedDays) {
        DateItemEvent dateItemEvent = new DateItemEvent();
        dateItemEvent.type = Type.DATE_NO_CHANGED;
        dateItemEvent.time = time;
        dateItemEvent.memoText = memoText;
        dateItemEvent.selectedDays = selectedDays;
        return dateItemEvent;
    }

    public static DateItemEvent newDateAdded() {
        DateItemEvent event = new DateItemEvent();
        event.type = Type.DATE_ADDED;
        return event;
    }

    public int getDateEnabled() {
        return dateEnabled;
    }

    public ArrayList<String> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(ArrayList<String> selectedDays) {
        this.selectedDays = selectedDays;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemoText() {
        return memoText;
    }

    public void setMemoText(String memoText) {
        this.memoText = memoText;
    }

    public Type getType() {
        return type;
    }

    public String getTimePosition() {
        return timePosition;
    }

    public void setDateEnabled(int dateEnabled) {
        this.dateEnabled = dateEnabled;
    }

    public void setTimePosition(String timePosition) {
        this.timePosition = timePosition;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
