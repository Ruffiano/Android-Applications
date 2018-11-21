package com.example.user.demo.activity;

import android.app.PendingIntent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.example.user.demo.R;
import com.example.user.demo.database.DatabaseHelper;
import com.example.user.demo.event.DateItemEvent;
import com.example.user.demo.models.DateItem;
import com.example.user.demo.service.SendAlarm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by User on 02.02.2016.
 */
public class DateDetailActivity extends BaseActivity {

    @Bind(R.id.timeT)
    TextView timeT;
    @Bind(R.id.timePosition)
    TextView timePosition;
    @Bind(R.id.memoT)
    EditText memoT;
    @Bind({R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat, R.id.sun})
    List<ToggleButton> toggleButtons;
    @Bind(R.id.switchAlarm)
    SwitchCompat switchAlarm;

    public ArrayList<String> strings;
    public ArrayList<String> selectedDays;
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    EventBus bus;
    DateItem dateItemBody;
    DateItem dateItem;
    private boolean isNew = true;
    DatabaseHelper db;
    private PendingIntent pendingIntent;
    int hourM, minM, time_position;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_detail);
        setHomeAsUp();

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.bpWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        gson = new Gson();
        bus = EventBus.getDefault();


    }

    @OnClick(R.id.timeT)
    void clickTime() {
        String[] splitTime = timeT.getText().toString().replace(" ", "").split(":");
        final RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment radialTimePickerDialogFragment, int hour, int min) {
                        timeT.setText(hour + " : " + min);
                        hourM = hour;
                        minM = min;
                        if (hour < 12 && hour > 0) {
                            // AM
                            timePosition.setText("AM");
                        } else {
                            // PM
                            timePosition.setText("PM");
                        }
                    }
                })
                .setStartTime(Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]))
                .setThemeCustom(R.style.MyDatePicker);
        rtpd.show(getSupportFragmentManager(), "");

    }

    private void dispalyDate() {
        if (dateItemBody != null) {
            timeT.setText(dateItemBody.getHour() + " : " + dateItemBody.getMinutes());
            memoT.setText(dateItemBody.getMemoText());
            timePosition.setText(dateItemBody.getTimePosition());
            if (dateItemBody.getDateEnable() == 1) {
                switchAlarm.setChecked(true);
            } else if (dateItemBody.getDateEnable() == 0) {
                switchAlarm.setChecked(false);
            }
            selectedDays = new ArrayList<String>();
            if (dateItemBody.getSelectedDays() != null)
                for (int i = 0; i < days.length; i++) {
                    if (dateItemBody.getSelectedDays().contains(days[i])) {
                        toggleButtons.get(i).setChecked(true);
                    }
                }
            isNew = false;
        } else {
            hourM = getIntent().getIntExtra("Hour", 10);
            minM = getIntent().getIntExtra("Min", 11);
            time_position = getIntent().getIntExtra("time_position", 12);
            if (time_position == 0) {
                timePosition.setText("AM");
            } else if (time_position == 1) {
                timePosition.setText("PM");
            }
            timeT.setText(hourM + " : " + minM);
            isNew = true;
        }
        for (int j = 0; j < toggleButtons.size(); j++) {
            final int finalI = j;

            strings = new ArrayList<String>();

            toggleButtons.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toggleButtons.get(finalI).isChecked()) {
                        strings.add(days[finalI]);
                    } else {
                        strings.remove(days[finalI]);
                    }
                }
            });

        }

    }

    @Nullable
    @OnClick(R.id.switchAlarm)
    void clickSwitchAlarm() {

//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//
//        if (dateItemBody != null) {
//            if (switchAlarm.isChecked()) {
//                dateItemBody.setDateEnable(1);
//            } else {
//                dateItemBody.setDateEnable(0);
//            }
//            dbHelper.updateDate(dateItemBody);
//
//            SendAlarm alarm = new SendAlarm();
//            alarm.cancelAlarm(this, dateItemBody.getId());
//            alarm.cancelNotification(this, dateItemBody.getId());
//
//            Toast.makeText(this, "Switch", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_date_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            sendDateToFragment();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendDateToFragment() {
        db = new DatabaseHelper(DateDetailActivity.this);

        dateItem = new DateItem();
        Gson gson = new Gson();
        if (isNew) {
            DateItemEvent event = new DateItemEvent();
            event.setHour(hourM);
            event.setMinutes(minM);
            event.setMemoText(memoT.getText().toString());
            event.setTimePosition(timePosition.getText().toString());
            if (switchAlarm.isChecked()) {
                event.setDateEnabled(1);
            } else {
                event.setDateEnabled(0);
            }
            event.setSelectedDays(strings);

            dateItem.setHour(hourM);
            dateItem.setMinutes(minM);
            dateItem.setMemoText(memoT.getText().toString());
            String inputJson = gson.toJson(strings);
            dateItem.setTimePosition(timePosition.getText().toString());
            if (switchAlarm.isChecked()) {
                dateItem.setDateEnable(1);
            } else {
                dateItem.setDateEnable(0);
            }
            dateItem.setDays(inputJson);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> selectedDays = gson.fromJson(inputJson, type);

            dateItem.setSelectedDays(selectedDays);
            dateItem.setId(db.getNextID("table_date"));
            db.addDate(dateItem);


            SendAlarm sendAlarm = new SendAlarm();
            sendAlarm.setAlarm(this, dateItem);
            bus.post(event);
        } else if (!isNew || !(memoT.getText().toString()).equals(dateItemBody.getMemoText()) || !timeT.getText().toString().equals(dateItemBody.getTime())) {
            dateItem.setId(dateItemBody.getId());
            if (hourM != 0 && minM != 0) {
                dateItem.setHour(hourM);
                dateItem.setMinutes(minM);
            } else {
                dateItem.setHour(dateItemBody.getHour());
                dateItem.setMinutes(dateItemBody.getMinutes());
            }
            dateItem.setMemoText(memoT.getText().toString());
            if (switchAlarm.isChecked()) {
                dateItem.setDateEnable(1);
            } else {
                dateItem.setDateEnable(0);
            }
            dateItemBody.getSelectedDays().addAll(strings);
            dateItem.setSelectedDays(dateItemBody.getSelectedDays());
            String inputJson = gson.toJson(dateItemBody.getSelectedDays());
            dateItem.setDays(inputJson);
            db.updateDate(dateItem);

            SendAlarm sendAlarm = new SendAlarm();
            sendAlarm.setAlarm(this, dateItem);
            bus.post(DateItemEvent.dateItemChanged(dateItem.getId(), hourM, minM, memoT.getText().toString(), strings));
        }

        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
//
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int id = getIntent().getIntExtra(DateItem.DATE_EXTRA_ID, 0);
        if (id == 0) {
            finish();
        }

        if (id == DateItem.DATE_FROM_PUSH) {

            int dateId = getIntent().getIntExtra("dateID", 0);
            dateItemBody = dbHelper.getDateItem(dateId);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> selectedDays = gson.fromJson(dateItemBody.getDays(), type);
            dateItemBody.setSelectedDays(selectedDays);
            if (dateItemBody == null) {
                finish();
                return;
            }
        } else if (id == DateItem.DATE_FROM_LIST || id == DateItem.DATE_NEW) {

            String bodyDate = getIntent().getStringExtra("DATE_BODY");

            dateItemBody = gson.fromJson(bodyDate, DateItem.class);
        }

        dispalyDate();

    }


}
