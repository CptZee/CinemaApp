package com.github.cptzee.cinemaapp.Data.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.cptzee.cinemaapp.Data.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieHelper extends SQLiteOpenHelper {
    public MovieHelper(Context context) {
        super(context, "MovieDB", null, 1);
    }
    private String TABLENAME = "Movies";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "id INTEGER," +
                    "title TEXT," +
                    "description TEXT," +
                    "rating TEXT," +
                    "categoryID INTEGER," +
                    "cinemaID INTEGER," +
                    "active INTEGER);");
        }catch (SQLiteException e){
            Log.e("Database", "Error creating the movies table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME + ";");
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("Database", "Error while upgrading the movies table");
        }
    }

    public void insert(Movie data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while inserting into the movies table");
        }
    }

    public void remove(Movie data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = { Integer.toString(data.getId()) };
        ContentValues content = new ContentValues();
        content.put("active", 0);
        try{
            db.update(TABLENAME, content, data.getId() + "= ?", where);
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while removing the " + data.getTitle() + " movie");
        }
    }

    public void update(Movie data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {Integer.toString(data.getId())};
        try{
            db.update(TABLENAME, prepareData(data), data.getId() + " = ?", where );
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while updating the " + data.getTitle() + " movie");
        }
    }

    public List<Movie> get(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Movie> list = new ArrayList<>();
        try{
            Cursor reader = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (reader.moveToNext()){
                Movie data = prepareData(reader);
                list.add(data);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while retrieving the movie");
        }
        return list;
    }

    public int count() {
        List<Movie> list = get();
        return list.size();
    }

    private ContentValues prepareData(Movie data){
        ContentValues content = new ContentValues();
        content.put("title", data.getTitle());
        content.put("description", data.getDescription());
        content.put("rating", data.getRating());
        content.put("categoryID", data.getCategoryID());
        content.put("cinemaID", data.getCinemaID());
        return content;
    }

    private Movie prepareData(Cursor cursor){
        Movie data = new Movie();
        data.setId(cursor.getInt(0));
        data.setTitle(cursor.getString(1));
        data.setDescription(cursor.getString(2));
        data.setRating(cursor.getString(3));
        data.setCategoryID(cursor.getInt(4));
        data.setCinemaID(cursor.getInt(5));
        return data;
    }
}
