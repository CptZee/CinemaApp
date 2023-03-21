package com.github.cptzee.cinemaapp.Data.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.cptzee.cinemaapp.Data.Cinema;
import com.github.cptzee.cinemaapp.Data.Movie;
import com.github.cptzee.cinemaapp.Data.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CinemaHelper extends SQLiteOpenHelper {
    public CinemaHelper(Context context) {
        super(context, "CinemaDB", null, 1);
    }
    private String TABLENAME = "Cinemas";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "id INTEGER," +
                    "name TEXT," +
                    "active INTEGER);");
        }catch (SQLiteException e){
            Log.e("Database", "Error creating the cinema table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME + ";");
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("Database", "Error while upgrading the cinema table");
        }
    }

    public void insert(Cinema data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while inserting into the category table");
        }
    }

    public void remove(Cinema data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = { Integer.toString(data.getId()) };
        ContentValues content = new ContentValues();
        content.put("active", 0);
        try{
            db.update(TABLENAME, content, data.getId() + "= ?", where);
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while removing the " + data.getName() + " category");
        }
    }

    public void update(Cinema data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {Integer.toString(data.getId())};
        try{
            db.update(TABLENAME, prepareData(data), data.getId() + " = ?", where );
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while updating the " + data.getName() + " account");
        }
    }

    public List<Cinema> get(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Cinema> list = new ArrayList<>();
        try{
            Cursor reader = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (reader.moveToNext()){
                Cinema data = prepareData(reader);
                list.add(data);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while retrieving the accounts");
        }
        return list;
    }

    public int count() {
        List<Cinema> list = get();
        return list.size();
    }

    private ContentValues prepareData(Cinema data){
        ContentValues content = new ContentValues();
        content.put("name", data.getName());
        return content;
    }

    private Cinema prepareData(Cursor cursor){
        Cinema data = new Cinema();
        data.setId(cursor.getInt(0));
        data.setName(cursor.getString(1));
        return data;
    }

    //Class specific method
    public Cinema getCinema(Movie data){
        AtomicReference<Cinema> cinema = null;
        get().forEach((cin) -> {
            if(data.getCinemaID() == cin.getId())
                cinema.set(cin);
        });
        return cinema.get();
    }

    //Class specific method
    public Cinema getCinema(Schedule data){
        AtomicReference<Cinema> cinema = null;
        get().forEach((cin) -> {
            if(data.getCinemaID() == cin.getId())
                cinema.set(cin);
        });
        return cinema.get();
    }
}
