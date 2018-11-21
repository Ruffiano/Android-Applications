package com.example.user.demo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.demo.R;
import com.example.user.demo.fragment.BaseFragment;
import com.example.user.demo.fragment.PilotFragment;
import com.example.user.demo.fragment.TimeTableFragment;
import com.example.user.demo.fragment.VideoFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 01.02.2016.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    public static final int PILOT_POS = 0;
    public static final int TIMETABLE_POS = 1;
    public static final int VIDEO_POS = 2;
    private List<BaseFragment> fragments;

    private Context context;

    public MainPagerAdapter(Context context, FragmentManager fm, String macAddress) {
        super(fm);
        fragments = Arrays.asList(
                new PilotFragment().newInstance(macAddress),
                new TimeTableFragment(),
                new VideoFragment()
        );
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case PILOT_POS:
                return context.getString(R.string.pilot);
            case TIMETABLE_POS:
                return context.getString(R.string.time_table);
            case VIDEO_POS:
                return context.getString(R.string.video);
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
