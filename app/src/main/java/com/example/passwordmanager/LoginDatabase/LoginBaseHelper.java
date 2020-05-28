package com.example.passwordmanager.LoginDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.passwordmanager.LoginDatabase.LoginDbSchema.*;

public class LoginBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "loginBase.db";

    public LoginBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LoginTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                LoginTable.Cols.UUID + ", " +
                LoginTable.Cols.USERNAME + ", " +
                LoginTable.Cols.PASSWORD + ", " +
                LoginTable.Cols.SALT +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
