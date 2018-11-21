package com.example.user.demo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.user.demo.R;
import com.example.user.demo.database.DatabaseHelper;
import com.example.user.demo.models.DateItem;
import com.example.user.demo.service.SendAlarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by User on 02.02.2016.
 */
public class DatePickerAdapter extends RecyclerView.Adapter<DatePickerAdapter.ViewHolder> {


    private Context context;
    private List<DateItem> dateItems;
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    String[] daysOb = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private ItemListener itemListener;

    DatabaseHelper databaseHelper;

    public DatePickerAdapter(Context context, List<DateItem> dateItems, ItemListener itemListener) {
        this.context = context;
        this.dateItems = dateItems;
        this.itemListener = itemListener;
        databaseHelper = DatabaseHelper.getInstance(this.context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.data_picker_adapter, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder vh, int i) {

        final DateItem dateItem = getItem(i);
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < days.length; j++) {
            if (dateItem.getSelectedDays() != null) {
                if (dateItem.getSelectedDays().contains(days[j])) {
                    stringBuilder.append(daysOb[j]);
                    stringBuilder.append(",");
                }
            }
        }
        if (dateItem.getDateEnable() == 1) {
            vh.switchAlarm.setChecked(true);
        } else if (dateItem.getDateEnable() == 0) {
            vh.switchAlarm.setChecked(false);
        }
        if (stringBuilder.length() > 0) {
            int id = stringBuilder.lastIndexOf(",");
            stringBuilder.deleteCharAt(id);
        }
        if (stringBuilder.length() == 27) {
            vh.listOfDays.setText("Every day");
        } else {
            vh.listOfDays.setText(stringBuilder);

        }


        if (dateItem.getMinutes() < 10) {
            vh.textView.setText(dateItem.getHour() + " : 0" + dateItem.getMinutes());
        } else {
            vh.textView.setText(dateItem.getHour() + " : " + dateItem.getMinutes());
        }


    }

    public DateItem getItem(int position) {
        if (dateItems != null && position >= 0 && position < getItemCount()) {
            return dateItems.get(position);
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return dateItems != null ? dateItems.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind({R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat, R.id.sun})
        List<ToggleButton> toggleButtons;

        @Bind(R.id.listOfDays)
        TextView listOfDays;
        @Bind(R.id.timeAlarm)
        TextView textView;
        @Bind(R.id.timePosition)
        TextView timePosition;
        @Bind(R.id.switchAlarm)
        SwitchCompat switchAlarm;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Nullable
        @OnClick({R.id.toggleContainers, R.id.timeAlarm, R.id.timePosition})
        void onItemClick() {
            DateItem dateItem = getItem(getAdapterPosition());
            if (dateItem != null && itemListener != null) {
                itemListener.onItemClick(dateItem);
            }
        }

        @Nullable
        @OnClick(R.id.switchAlarm)
        void clickAlarm() {

//            DateItem dateItem = getItem(getAdapterPosition());
//            SendAlarm alarm = new SendAlarm();
//            if (switchAlarm.isChecked()) {
//                dateItem.setDateEnable(1);
//                alarm.setAlarm(context, dateItem);
//            } else {
//                dateItem.setDateEnable(0);
//                alarm.cancelAlarm(context, dateItem.getId());
//                alarm.cancelNotification(context, dateItem.getId());
//            }
//            databaseHelper.updateDate(dateItem);
//
//
//            Toast.makeText(context, "Switch", Toast.LENGTH_SHORT).show();

        }

        public void updateItem() {
            notifyItemChanged((getAdapterPosition()));
        }

        @Nullable
        @OnLongClick(R.id.dateContainer)
        boolean onLongItemClick() {
            DateItem dateItem = getItem(getAdapterPosition());
            if (dateItem != null && itemListener != null) {
                itemListener.onLongItemClick(dateItem, getAdapterPosition());
            }
            return true;
        }


    }

    public void delete(int position) {
        dateItems.remove(position);
    }


    public interface ItemListener {
        void onItemClick(DateItem dateItem);

        void onLongItemClick(DateItem dateItem, int position);
    }

}
