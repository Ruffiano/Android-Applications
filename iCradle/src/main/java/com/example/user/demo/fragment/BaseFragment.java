package com.example.user.demo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.demo.application.DemoApplication;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by User on 03.02.2016.
 */
public abstract class BaseFragment extends Fragment {


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((DemoApplication) getActivity().getApplication()).inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @LayoutRes
    protected abstract int getLayout();

    protected void registerEventSubscriber() {
        EventBus.getDefault().register(this);
    }

    protected boolean hasActivity() {
        return getActivity() != null && !getActivity().isFinishing();
    }


}
