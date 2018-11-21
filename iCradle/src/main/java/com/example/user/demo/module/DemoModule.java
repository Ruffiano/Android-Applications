package com.example.user.demo.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.demo.activity.BaseActivity;
import com.example.user.demo.activity.BluetoothListActivity;
import com.example.user.demo.activity.DateDetailActivity;
import com.example.user.demo.activity.LoginActivity;
import com.example.user.demo.activity.MainActivity;
import com.example.user.demo.activity.Motion;
import com.example.user.demo.activity.SignActivity;
import com.example.user.demo.adapter.DatePickerAdapter;
import com.example.user.demo.application.DemoApplication;
import com.example.user.demo.fragment.PilotFragment;
import com.example.user.demo.fragment.TimeTableFragment;
import com.example.user.demo.fragment.VideoFragment;
import com.example.user.demo.utils.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 01.02.2016.
 */

@Module(
        library = true,
        injects = {
                DemoApplication.class,
                MainActivity.class,
                PilotFragment.class,
                TimeTableFragment.class,
                VideoFragment.class,
                BaseActivity.class,
                DatePickerAdapter.class,
                DateDetailActivity.class,
                LoginActivity.class,
                SignActivity.class,
                Motion.class,
                BluetoothListActivity.class,
        }
)

public class DemoModule {

    private Application application;

    public DemoModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Settings provideSettings(SharedPreferences preferences) {
        return new Settings(preferences);
    }
}
