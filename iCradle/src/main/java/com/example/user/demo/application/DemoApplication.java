package com.example.user.demo.application;

import android.app.Application;
import android.content.Context;

import com.example.user.demo.models.DateItem;
import com.example.user.demo.module.DemoModule;
import com.example.user.demo.utils.Settings;

import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by User on 23.10.2015.
 */
public class DemoApplication extends Application {
    ObjectGraph graph;
    public static Context context;
    @Inject
    Settings settings;

    private static DateItem dateItem;

    public static List<String> selectedDays;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        graph = ObjectGraph.create(new DemoModule(this));
        inject(this);

    }

    public static DateItem getCurrentDate() {
        return dateItem;
    }


    public void inject(Object object) {
        graph.inject(object);
    }

    public static Context getAppContext() {
        return DemoApplication.context;
    }

    public static void setContext(Context mContext) {
        context = mContext;
    }

}
