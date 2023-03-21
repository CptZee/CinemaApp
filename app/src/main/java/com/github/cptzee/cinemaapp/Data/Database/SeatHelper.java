package com.github.cptzee.cinemaapp.Data.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.cptzee.cinemaapp.Data.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatHelper extends SQLiteOpenHelper {
    public SeatHelper(Context context) {
        super(context, "SeatDB", null, 1);
    }
    private String TABLENAME = "Seats";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "id INTEGER," +
                    "scheduleID INTEGER," +
                    "seat TEXT," +
                    "active INTEGER);");
        }catch (SQLiteException e){
            Log.e("Database", "Error creating the seats table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME + ";");
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("Database", "Error while upgrading the seats table");
        }
    }

    public void insert(Seat data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while inserting into the seats table");
        }
    }

    public void remove(Seat data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = { Integer.toString(data.getId()) };
        ContentValues content = new ContentValues();
        content.put("active", 0);
        try{
            db.update(TABLENAME, content, data.getId() + "= ?", where);
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while removing the " + data.getSeat() + " seat");
        }
    }

    public void update(Seat data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {Integer.toString(data.getId())};
        try{
            db.update(TABLENAME, prepareData(data), data.getId() + " = ?", where );
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while updating the " + data.getSeat() + " seat");
        }
    }

    public List<Seat> get(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Seat> list = new ArrayList<>();
        try{
            Cursor reader = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (reader.moveToNext()){
                Seat data = prepareData(reader);
                list.add(data);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while retrieving the seat");
        }
        return list;
    }

    public int count() {
        List<Seat> list = get();
        return list.size();
    }

    private ContentValues prepareData(Seat data){
        ContentValues content = new ContentValues();
        content.put("scheduleID", data.getScheduleID());
        content.put("seat", data.getSeat());
        return content;
    }

    private Seat prepareData(Cursor cursor){
        Seat data = new Seat();
        data.setId(cursor.getInt(0));
        data.setScheduleID(cursor.getInt(1));
        data.setSeat(cursor.getString(2));
        return data;
    }
}
