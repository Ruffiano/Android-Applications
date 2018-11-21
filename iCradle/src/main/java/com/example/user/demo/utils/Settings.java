package com.example.user.demo.utils;

import android.content.SharedPreferences;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by User on 02.02.2016.
 */

@Singleton
public class Settings {

    private final SharedPreferences preferences;

    @Inject
    public Settings(SharedPreferences preferences) {
        this.preferences = preferences;
    }


    public Set<String> getDays(String day) {
        return preferences.getStringSet(day, null);
    }

    public boolean dayIsEnabled(String day) {
        return preferences.getBoolean(day, false);
    }


    public void setCurrentId(int id) {
        preferences.edit().putInt("id", id).apply();
    }

    private int getCurretnId() {
        return preferences.getInt("id", 10);
    }


    public void saveAuth(boolean auth) {
        preferences.edit().putBoolean("AUTH", auth).apply();
    }

    public Boolean getAuth() {
        return preferences.getBoolean("AUTH", false);
    }


}
