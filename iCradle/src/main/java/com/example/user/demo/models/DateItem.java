package com.example.user.demo.models;

import java.util.ArrayList;

/**
 * Created by User on 02.02.2016.
 */
public class DateItem {

    public static final String DATE_EXTRA_ID = "current_date_id";
    public static final int DATE_FROM_PUSH = 101;
    public static final int DATE_FROM_LIST = 102;
    public static final int DATE_NEW = 103;

    int id;
    String time;
    Integer hour;
    Integer minutes;
    String memoText;
    String timePosition;
    Integer dateEnable;
    String days;
    ArrayList<String> selectedDays;
    Boolean isComplate;

    Boolean isReapetMode;

    public DateItem(int id, int hour, int minutes, String memoText, String timePosition, int dateEnable, String days) {
        this.id = id;
        this.hour = hour;
        this.minutes = minutes;
        this.memoText = memoText;
        this.timePosition = timePosition;
        this.dateEnable = dateEnable;
        this.days = days;
    }

    public DateItem(int id, int hour, int minutes, String memoText, String timePosition, int dateEnable, ArrayList<String> selectedDays) {
        this.id = id;
        this.hour = hour;
        this.minutes = minutes;
        this.memoText = memoText;
        this.timePosition = timePosition;
        this.dateEnable = dateEnable;
        this.selectedDays = selectedDays;
    }

    public DateItem() {
    }

    public String getTimePosition() {
        return timePosition;
    }

    public void setTimePosition(String timePosition) {
        this.timePosition = timePosition;
    }

    public String getDays() {
        return days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getMemoText() {
        return memoText;
    }

    public void setMemoText(String memoText) {
        this.memoText = memoText;
    }

    public ArrayList<String> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(ArrayList<String> selectedDays) {
        this.selectedDays = selectedDays;
    }

    public String getTime() {
        return hour + ":" + minutes;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getIsReapetMode() {
        return isReapetMode;
    }

    public void setIsReapetMode(Boolean isReapetMode) {
        this.isReapetMode = isReapetMode;
    }

    public Integer getDateEnable() {
        return dateEnable != null ? dateEnable : 0;
    }

    public void setDateEnable(int dateEnable) {
        this.dateEnable = dateEnable;
    }

    public Integer getHour() {
        return hour != null ? hour : 0;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Integer getMinutes() {
        return minutes != null ? minutes : 0;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
