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
import java.util.concurrent.atomic.AtomicReference;

public class AccountHelper extends SQLiteOpenHelper{
    public AccountHelper(Context context) {
        super(context, "AccountDB", null, 1);
    }
    private String TABLENAME = "Accounts";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "id INTEGER," +
                    "firstName TEXT," +
                    "lastName TEXT," +
                    "email TEXT," +
                    "contactNo TEXT," +
                    "active INTEGER);");
        }catch (SQLiteException e){
            Log.e("Database", "Error creating the account table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME + ";");
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("Database", "Error while upgrading the account table");
        }
    }

    public void insert(Account data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while inserting into the accounts table");
        }
    }

    public void remove(Account data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = { Integer.toString(data.getId()) };
        ContentValues content = new ContentValues();
        content.put("active", 0);
        try{
            db.update(TABLENAME, content, data.getId() + "= ?", where);
            db.close();
        }catch (SQLiteException e){
            Log.e("Database", "Error while removing the " + data.getLastName() + " account");
        }
    }

    public void update(Account data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {Integer.toString(data.getId())};
        try{
            db.update(TABLENAME, prepareData(data), data.getId() + " = ?", where );
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while updating the " + data.getLastName() + " account");
        }
    }

    public List<Account> get(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Account> list = new ArrayList<>();
        try{
            Cursor reader = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (reader.moveToNext()){
                Account data = prepareData(reader);
                list.add(data);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Database", "Error while retrieving the accounts");
        }
        return list;
    }

    public int count() {
        List<Account> list = get();
        return list.size();
    }

    private ContentValues prepareData(Account data){
        ContentValues content = new ContentValues();
        content.put("firstName", data.getFirstName());
        content.put("lastName", data.getLastName());
        content.put("email", data.getEmail());
        content.put("contactNo", data.getContactNo());
        return content;
    }

    private Account prepareData(Cursor cursor){
        Account data = new Account();
        data.setId(cursor.getInt(0));
        data.setFirstName(cursor.getString(1));
        data.setLastName(cursor.getString(2));
        data.setEmail(cursor.getString(3));
        data.setContactNo(cursor.getString(4));
        return data;
    }

    //Class Specific Method

    private Account getAccount(Credential credential){
        SQLiteDatabase db = this.getWritableDatabase();
        AtomicReference<Account> account = null;
        try{
            get().forEach((acc) -> {
                if(acc.getId() == credential.getAccountID())
                    account.set(acc);
            });
        }catch (SQLiteException e){
            Log.e("Database", "Unable to retrieve the account using the credentials");
        }
        return account.get();
    }
}
