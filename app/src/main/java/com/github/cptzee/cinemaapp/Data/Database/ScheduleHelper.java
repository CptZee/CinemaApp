package com.github.cptzee.cinemaapp.Data.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.cptzee.cinemaapp.Data.Cinema;
import com.github.cptzee.cinemaapp.Data.Schedule;
import com.github.cptzee.cinemaapp.Data.Seat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class ScheduleHelper extends SQLiteOpenHelper {
    public ScheduleHelper(Context context) {
        super(context, "ScheduleDB", null, 1);
    }
    private String TABLENAME = "Schedules";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "id INTEGER," +
                    "cinemaID INTEGER," +
                    "time INTEGER," +
                    "active INTEGER);");
        }catch (SQLiteException e){
            Log.e("Database", "Error creating the schedule table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME + ";");
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("Database", "Error while upgrading the schedule table");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insert(Schedule data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while inserting into the schedule table");
        }
    }

    public void remove(Schedule data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = { Integer.toString(data.getId()) };
        ContentValues content = new ContentValues();
        content.put("active", 0);
        try{
            db.update(TABLENAME, content, data.getId() + "= ?", where);
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while removing the " + data.getId() + " schedule");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(Schedule data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {Integer.toString(data.getId())};
        try{
            db.update(TABLENAME, prepareData(data), data.getId() + " = ?", where );
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while updating the " + data.getId() + " schedule");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Schedule> get(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Schedule> list = new ArrayList<>();
        try{
            Cursor reader = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (reader.moveToNext()){
                Schedule data = prepareData(reader);
                list.add(data);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while retrieving the schedules");
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int count() {
        List<Schedule> list = get();
        return list.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ContentValues prepareData(Schedule data){
        long time = data.getTime().toEpochSecond(ZoneOffset.UTC);
        ContentValues content = new ContentValues();
        content.put("cinemaID", data.getCinemaID());
        content.put("time", time);

        return content;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Schedule prepareData(Cursor cursor){
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(cursor.getLong(2)),
                TimeZone.getDefault().toZoneId());
        Schedule data = new Schedule();
        data.setId(cursor.getInt(0));
        data.setCinemaID(cursor.getInt(1));
        data.setTime(time);
        return data;
    }


    //Class specific method
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Schedule getSchedule(Seat data){
        AtomicReference<Schedule> cinema = null;
        get().forEach((cin) -> {
            if(data.getScheduleID() == cin.getId())
                cinema.set(cin);
        });
        return cinema.get();
    }
}
