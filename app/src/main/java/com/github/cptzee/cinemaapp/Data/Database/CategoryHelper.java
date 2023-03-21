package com.github.cptzee.cinemaapp.Data.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.cptzee.cinemaapp.Data.Category;
import com.github.cptzee.cinemaapp.Data.Cinema;
import com.github.cptzee.cinemaapp.Data.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CategoryHelper extends SQLiteOpenHelper {
    public CategoryHelper(Context context) {
        super(context, "CategoryDB", null, 1);
    }
    private String TABLENAME = "Categories";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "id INTEGER," +
                    "name TEXT," +
                    "active INTEGER);");
        }catch (SQLiteException e){
            Log.e("Database", "Error creating the category table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME + ";");
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("Database", "Error while upgrading the category table");
        }
    }

    public void insert(Category data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while inserting into the category table");
        }
    }

    public void remove(Category data) {
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

    public void update(Category data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {Integer.toString(data.getId())};
        try{
            db.update(TABLENAME, prepareData(data), data.getId() + " = ?", where );
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while updating the " + data.getName() + " account");
        }
    }

    public List<Category> get(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Category> list = new ArrayList<>();
        try{
            Cursor reader = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (reader.moveToNext()){
                Category data = prepareData(reader);
                list.add(data);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while retrieving the accounts");
        }
        return list;
    }

    public int count() {
        List<Category> list = get();
        return list.size();
    }

    private ContentValues prepareData(Category data){
        ContentValues content = new ContentValues();
        content.put("name", data.getName());
        return content;
    }

    private Category prepareData(Cursor cursor){
        Category data = new Category();
        data.setId(cursor.getInt(0));
        data.setName(cursor.getString(1));
        return data;
    }

    //Class specific method
    public Category getCategory(Movie data){
        AtomicReference<Category> cinema = null;
        get().forEach((cat) -> {
            if(data.getCategoryID() == cat.getId())
                cinema.set(cat);
        });
        return cinema.get();
    }
}
