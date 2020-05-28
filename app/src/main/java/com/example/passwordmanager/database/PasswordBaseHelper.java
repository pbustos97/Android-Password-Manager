package com.example.passwordmanager.database;

import android.content.Context;
import android.util.Log;

import com.example.passwordmanager.database.PasswordDbSchema.PasswordTable;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class PasswordBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static PasswordBaseHelper instance;

    public PasswordBaseHelper(Context context, String uuid) {
            super(context, uuid, null, VERSION);
            Log.d("PasswordBaseHelper", uuid);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PasswordTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PasswordTable.Cols.UUID + ", " +
                PasswordTable.Cols.SERVICE + ", " +
                PasswordTable.Cols.USERNAME + ", " +
                PasswordTable.Cols.PASSWORD + ", " +
                PasswordTable.Cols.LENGTH + ", " +
                PasswordTable.Cols.VERSION + ", " +
                PasswordTable.Cols.DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public static synchronized PasswordBaseHelper getInstance(Context context, String uuid) {
        instance = new PasswordBaseHelper(context, uuid);
        return instance;
    }
}
