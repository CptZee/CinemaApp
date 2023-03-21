package com.github.cptzee.cinemaapp.Data.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.cptzee.cinemaapp.Data.Account;
import com.github.cptzee.cinemaapp.Data.Credential;

import java.util.ArrayList;
import java.util.List;

public class CredentialHelper extends SQLiteOpenHelper {
    public CredentialHelper(Context context) {
        super(context, "CredentialDB", null, 1);
    }
    private String TABLENAME = "Credentials";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "id INTEGER," +
                    "accountID INTEGER," +
                    "username TEXT," +
                    "password TEXT," +
                    "active INTEGER);");
        }catch (SQLiteException e){
            Log.e("Database", "Error creating the " + TABLENAME  + " table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME + ";");
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("Database", "Error while upgrading the " + TABLENAME  + " table");
        }
    }

    public void insert(Credential data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while inserting into the credentials table");
        }
    }

    public void remove(Credential data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = { Integer.toString(data.getId()) };
        ContentValues content = new ContentValues();
        content.put("active", 0);
        try{
            db.update(TABLENAME, content, data.getId() + "= ?", where);
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while removing the " + data.getAccountID() + " credentials");
        }
    }

    public void update(Credential data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {Integer.toString(data.getId())};
        try{
            db.update(TABLENAME, prepareData(data), data.getId() + " = ?", where );
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while updating the " + data.getAccountID() + " credential");
        }
    }

    public List<Credential> get(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Credential> list = new ArrayList<>();
        try{
            Cursor reader = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (reader.moveToNext()){
                Credential data = prepareData(reader);
                list.add(data);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while retrieving the accounts");
        }
        return list;
    }

    public int count() {
        List<Credential> list = get();
        return list.size();
    }

    private ContentValues prepareData(Credential data){
        ContentValues content = new ContentValues();
        content.put("accountID", data.getAccountID());
        content.put("username", data.getUsername());
        content.put("password", data.getPassword());
        return content;
    }

    private Credential prepareData(Cursor cursor){
        Credential data = new Credential();
        data.setId(cursor.getInt(0));
        data.setAccountID(cursor.getInt(1));
        data.setUsername(cursor.getString(2));
        data.setPassword(cursor.getString(3));
        return data;
    }
}
