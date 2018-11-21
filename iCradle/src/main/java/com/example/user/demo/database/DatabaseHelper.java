package com.example.user.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.demo.models.DateItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 02.02.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "demo2";

    private static final String TABLE_DATE = "table_date";
    private static final String DATE_ID = "date_id";
    private static final String DATE_TIME_HOUR = "date_time_hour";
    private static final String DATE_TIME_MINUTES = "date_time_minutes";
    private static final String DATE_TIME_ENABLE = "date_time_enable";
    private static final String DATE_MEMO = "date_memo";
    private static final String DATE_TIME_POSITION = "date_time_position";
    private static final String DATE_SELECTED_DAYS = "selected_days";

    private static final String CREATE_DATE = "create table " + TABLE_DATE
            + " (" + DATE_ID + " integer primary key autoincrement, "
            + DATE_TIME_HOUR + " int,"
            + DATE_TIME_MINUTES + " int,"
            + DATE_MEMO + " text,"
            + DATE_TIME_POSITION + " text,"
            + DATE_TIME_ENABLE + " integer,"
            + DATE_SELECTED_DAYS + " text);";

    static final String CREATE_LOGIN = "create table " + "LOGIN" +
            "( " + "ID" + " integer primary key autoincrement," + "USERNAME  text,PASSWORD text, MACADRESS text); ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        instance = new DatabaseHelper(context);
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATE);
        db.execSQL(CREATE_LOGIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addDate(DateItem dateItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE_ID, dateItem.getId());
        values.put(DATE_TIME_HOUR, dateItem.getHour());
        values.put(DATE_TIME_MINUTES, dateItem.getMinutes());
        values.put(DATE_MEMO, dateItem.getMemoText());
        values.put(DATE_TIME_POSITION, dateItem.getTimePosition());
        values.put(DATE_TIME_ENABLE, dateItem.getDateEnable());
        values.put(DATE_SELECTED_DAYS, dateItem.getDays());

        db.insert(TABLE_DATE, null, values);
        db.close();

    }

    public void updateDate(DateItem dateItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_TIME_HOUR, dateItem.getHour());
        values.put(DATE_TIME_MINUTES, dateItem.getMinutes());
        values.put(DATE_MEMO, dateItem.getMemoText());
        values.put(DATE_TIME_POSITION, dateItem.getTimePosition());
        values.put(DATE_TIME_ENABLE, dateItem.getDateEnable());
        values.put(DATE_SELECTED_DAYS, dateItem.getDays());

        int count = db.update(TABLE_DATE, values, DATE_ID + "=?", new String[]{String.valueOf(dateItem.getId())});
        db.close();
        System.out.println(count);
    }

    public void deleteDateItem(int id) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int count = db.delete(TABLE_DATE, DATE_ID + " = ?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getNextID(String table) {

        String selectQuery = "SELECT MAX(" + DATE_ID +
                ") FROM " + table;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int i = cursor.getInt(0) + 1;
            cursor.close();
            close();
            return i;
        } else {
            cursor.close();
            close();
            return 1;
        }
    }

    public DateItem getDateItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DATE + " WHERE "
                + DATE_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        DateItem dateItem = new DateItem();
        dateItem.setId(cursor.getInt(cursor.getColumnIndex(DATE_ID)));
        dateItem.setHour(cursor.getInt(cursor.getColumnIndex(DATE_TIME_HOUR)));
        dateItem.setMinutes(cursor.getInt(cursor.getColumnIndex(DATE_TIME_MINUTES)));
        dateItem.setMemoText(cursor.getString(cursor.getColumnIndex(DATE_MEMO)));
        dateItem.setTimePosition(cursor.getString(cursor.getColumnIndex(DATE_TIME_POSITION)));
        dateItem.setDateEnable(cursor.getInt(cursor.getColumnIndex(DATE_TIME_ENABLE)));
        dateItem.setDays(cursor.getString(cursor.getColumnIndex(DATE_SELECTED_DAYS)));

        return dateItem;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DATE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public List<DateItem> getAllDates() {
        List<DateItem> dateItems = new ArrayList<DateItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_DATE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DateItem dateItem = new DateItem();
                dateItem.setId(cursor.getInt(cursor.getColumnIndex(DATE_ID)));
                dateItem.setHour(cursor.getInt(cursor.getColumnIndex(DATE_TIME_HOUR)));
                dateItem.setMinutes(cursor.getInt(cursor.getColumnIndex(DATE_TIME_MINUTES)));
                dateItem.setMemoText(cursor.getString(cursor.getColumnIndex(DATE_MEMO)));
                dateItem.setTimePosition(cursor.getString(cursor.getColumnIndex(DATE_TIME_POSITION)));
                dateItem.setDateEnable(cursor.getInt(cursor.getColumnIndex(DATE_TIME_ENABLE)));
                dateItem.setDays(cursor.getString(cursor.getColumnIndex(DATE_SELECTED_DAYS)));
                dateItems.add(dateItem);
            } while (cursor.moveToNext());
        }

        return dateItems;
    }

    public void saveUser(String userName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD", password);


        // Insert the row into your table
        db.insert("LOGIN", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }

    public int deleteUser(String UserName) {
        SQLiteDatabase db = this.getWritableDatabase();

        //String id=String.valueOf(ID);
        String where = "USERNAME=?";
        int numberOFEntriesDeleted = db.delete("LOGIN", where, new String[]{UserName});
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    public String getSingleUser(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("LOGIN", null, "USERNAME=?", new String[]{userName}, null, null, null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        db.close();
        return password;
    }


    public void updateUser(String userName, String password, String MacAdress) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD", password);
        updatedValues.put("MACADRESS", MacAdress);

        String where = "USERNAME = ?";
        db.update("LOGIN", updatedValues, where, new String[]{userName});
    }

}
