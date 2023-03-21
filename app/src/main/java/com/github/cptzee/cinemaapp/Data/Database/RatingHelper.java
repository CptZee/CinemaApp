package com.github.cptzee.cinemaapp.Data.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.cptzee.cinemaapp.Data.Rating;

import java.util.ArrayList;
import java.util.List;

public class RatingHelper extends SQLiteOpenHelper {
    public RatingHelper(Context context) {
        super(context, "RatingDB", null, 1);
    }
    private String TABLENAME = "Ratings";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "id INTEGER," +
                    "name TEXT," +
                    "active INTEGER);");
        }catch (SQLiteException e){
            Log.e("Database", "Error creating the ratings table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME + ";");
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("Database", "Error while upgrading the ratings table");
        }
    }

    public void insert(Rating data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while inserting into the ratings table");
        }
    }

    public void remove(Rating data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = { Integer.toString(data.getId()) };
        ContentValues content = new ContentValues();
        content.put("active", 0);
        try{
            db.update(TABLENAME, content, data.getId() + "= ?", where);
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while removing the " + data.getName() + " rating");
        }
    }

    public void update(Rating data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {Integer.toString(data.getId())};
        try{
            db.update(TABLENAME, prepareData(data), data.getId() + " = ?", where );
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while updating the " + data.getName() + " rating");
        }
    }

    public List<Rating> get(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Rating> list = new ArrayList<>();
        try{
            Cursor reader = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (reader.moveToNext()){
                Rating data = prepareData(reader);
                list.add(data);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while retrieving the rating");
        }
        return list;
    }

    public int count() {
        List<Rating> list = get();
        return list.size();
    }

    private ContentValues prepareData(Rating data){
        ContentValues content = new ContentValues();
        content.put("name", data.getName());
        return content;
    }

    private Rating prepareData(Cursor cursor){
        Rating data = new Rating();
        data.setId(cursor.getInt(0));
        data.setName(cursor.getString(1));
        return data;
    }
}
