package com.example.user.demo.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.example.user.demo.R;
import com.example.user.demo.activity.DateDetailActivity;
import com.example.user.demo.adapter.DatePickerAdapter;
import com.example.user.demo.application.DemoApplication;
import com.example.user.demo.database.DatabaseHelper;
import com.example.user.demo.event.DateItemEvent;
import com.example.user.demo.models.DateItem;
import com.example.user.demo.service.SendAlarm;
import com.example.user.demo.utils.DividerItemDecoration;
import com.example.user.demo.utils.Settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by User on 01.02.2016.
 */
public class TimeTableFragment extends BaseFragment {
    @Inject
    Settings settings;

    private DatePickerAdapter dateAdapter;
    @Bind(R.id.fab)
    FloatingActionButton actionBtn;

    @Bind(R.id.timeLabel)
    TextView textLebel;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    final List<DateItem> values = new ArrayList<DateItem>();
    List<DateItem> valuesFromDb;
    DateItem dateItem;
    AlertDialog deleteDialog;
    DatabaseHelper db;
    Gson gson;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        db = new DatabaseHelper(getActivity());
        gson = new Gson();

        if (db != null && db.getAllDates() != null && db.getAllDates().size() > 0) {

            valuesFromDb = db.getAllDates();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            for (DateItem date : valuesFromDb) {

                ArrayList<String> finalOutputString = gson.fromJson(date.getDays(), type);
                dateItem = new DateItem(date.getId(), date.getHour(), date.getMinutes(), date.getMemoText(), date.getTimePosition(), date.getDateEnable(), finalOutputString);
                values.add(dateItem);
                dateAdapter = new DatePickerAdapter(getActivity(), values, itemListener);
                recyclerView.setAdapter(dateAdapter);
                dateAdapter.notifyDataSetChanged();
                textLebel.setVisibility(View.GONE);
            }
        }
        registerEventSubscriber();
    }

    private void reloadItems() {

        if (db != null && db.getAllDates() != null && db.getAllDates().size() > 0) {

            values.clear();
            valuesFromDb = db.getAllDates();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            for (DateItem date : valuesFromDb) {

                ArrayList<String> selectedDays = gson.fromJson(date.getDays(), type);
                dateItem = new DateItem(date.getId(), date.getHour(), date.getMinutes(), date.getMemoText(), date.getTimePosition(), date.getDateEnable(), selectedDays);
                values.add(dateItem);
                dateAdapter = new DatePickerAdapter(getActivity(), values, itemListener);
                recyclerView.setAdapter(dateAdapter);
                dateAdapter.notifyDataSetChanged();
                textLebel.setVisibility(View.GONE);
            }
        }
    }

    public void onEvent(DateItemEvent event) {

        if (event.getType() == DateItemEvent.Type.DATE_CHANGED || event.getType() == DateItemEvent.Type.DATE_NO_CHANGED) {
            reloadItems();
        } else {
            dateItem = new DateItem(event.getId(), event.getHour(), event.getMinutes(), event.getMemoText(), event.getTimePosition(), event.getDateEnabled(), event.getSelectedDays());
            values.add(dateItem);
            dateAdapter = new DatePickerAdapter(getActivity(), values, itemListener);
            recyclerView.setAdapter(dateAdapter);
            dateAdapter.notifyDataSetChanged();
            textLebel.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.fab)
    void createAlarm() {

        final RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment radialTimePickerDialogFragment, int hour, int min) {

                        Intent intent = new Intent(getActivity(), DateDetailActivity.class);
                        intent.putExtra("Hour", hour);
                        intent.putExtra("Min", min);
                        intent.putExtra(DateItem.DATE_EXTRA_ID,DateItem.DATE_NEW);
                        if (hour < 12 && hour > 0) {
                            // AM
                            intent.putExtra("time_position", 0);
                        } else {
                            // PM
                            intent.putExtra("time_position", 1);
                        }
                        getActivity().startActivity(intent);
                        Toast.makeText(getActivity(), "Hour : " + hour + " Min : " + min, Toast.LENGTH_SHORT).show();
                    }
                })
                .setStartTime(10, 10)
                .setThemeCustom(R.style.MyDatePicker);
        rtpd.show(getFragmentManager(), "");

    }

    DatePickerAdapter.ItemListener itemListener = new DatePickerAdapter.ItemListener() {
        @Override
        public void onItemClick(DateItem dateItem) {
            openDateDetail(dateItem);
            Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongItemClick(final DateItem dateItem, final int position) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
            builderSingle.setTitle("Test");
            builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dateAdapter.delete(position);
                    db.deleteDateItem(dateItem.getId());
                    SendAlarm sendAlarm = new SendAlarm();
                    sendAlarm.cancelAlarm(getActivity(), dateItem.getId());
                    sendAlarm.cancelNotification(getActivity(), dateItem.getId());

                    Toast.makeText(getActivity(), "Alarm is deleted", Toast.LENGTH_SHORT).show();
                    dateAdapter.notifyDataSetChanged();
                }
            });
            builderSingle.setNegativeButton("No", null);

            deleteDialog = builderSingle.create();
            deleteDialog.show();
        }
    };

    private void openDateDetail(DateItem dateItem) {
        Intent intent = new Intent(getActivity(), DateDetailActivity.class);
        gson = new Gson();
        String postJson = gson.toJson(dateItem);

        intent.putExtra("DATE_BODY", postJson);
        intent.putExtra(DateItem.DATE_EXTRA_ID, DateItem.DATE_FROM_LIST);
        startActivity(intent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((DemoApplication) getActivity().getApplication()).inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_time_table;
    }

}
