package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;

import com.example.passwordmanager.database.PasswordCursorWrapper;
import com.example.passwordmanager.database.PasswordBaseHelper;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.passwordmanager.database.PasswordDbSchema.*;

public class PasswordList {
    private static PasswordList sPasswordList;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private String mUuid;
    private String mUserPassword;

    public static PasswordList get(Context context, String uuid, String userPassword) {
        sPasswordList = new PasswordList(context, uuid, userPassword);
        return sPasswordList;
    }

    private PasswordList(Context context, String uuid, String userPassword){
        mContext = context.getApplicationContext();
        mUuid = uuid + ".db";
        mUserPassword = userPassword;
        SQLiteDatabase.loadLibs(mContext);
        mDatabase = new PasswordBaseHelper(mContext, mUuid).getInstance(mContext, mUuid).getWritableDatabase(mUserPassword);
    }

    public void addPassword(Password p) {
        SQLiteDatabase.loadLibs(mContext);
        ContentValues values = getContentValues(p);
        mDatabase.insert(PasswordTable.NAME, null, values);
    }

    public void updatePassword(Password p) {
        SQLiteDatabase.loadLibs(mContext);
        String uuidString = p.getId().toString();
        ContentValues values = getContentValues(p);

        mDatabase.update(PasswordTable.NAME, values,
                PasswordTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void deletePassword(Password p) {
        SQLiteDatabase.loadLibs(mContext);
        String uuidString = p.getId().toString();
        ContentValues values = getContentValues(p);

        mDatabase.delete(PasswordTable.NAME,
                PasswordTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private PasswordCursorWrapper queryPasswords(String whereClause, String[] whereArgs) {
        SQLiteDatabase.loadLibs(mContext);
        Cursor cursor = mDatabase.query(
                PasswordTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new PasswordCursorWrapper(cursor);
    }

    // Returns list of passwords
    public List<Password> getPasswords() {
        SQLiteDatabase.loadLibs(mContext);

        List<Password> passwords = new ArrayList<>();

        PasswordCursorWrapper cursor = queryPasswords(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                passwords.add(cursor.getPassword());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return passwords;
    }

    public Password getPassword(UUID id) {
        SQLiteDatabase.loadLibs(mContext);
        PasswordCursorWrapper cursor = queryPasswords(
                PasswordTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                cursor.close();
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPassword();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Password password) {
        ContentValues values = new ContentValues();
        values.put(PasswordTable.Cols.UUID, password.getId().toString());
        values.put(PasswordTable.Cols.SERVICE, password.getService());
        values.put(PasswordTable.Cols.USERNAME, password.getUsername());
        values.put(PasswordTable.Cols.PASSWORD, password.getPassword());
        values.put(PasswordTable.Cols.LENGTH, password.getLength());
        values.put(PasswordTable.Cols.VERSION, password.getIteration());
        values.put(PasswordTable.Cols.DATE, password.getDate().getTime());

        return values;
    }

    public void closeDb() {
        SQLiteDatabase.loadLibs(mContext);
        if (mDatabase.isOpen()) {
            mDatabase.close();
        }
    }

    public void openDb() {
        SQLiteDatabase.loadLibs(mContext);
        if (!mDatabase.isOpen()) {
            mDatabase = new PasswordBaseHelper(mContext, mUuid).getWritableDatabase(mUserPassword);
        }
    }
}
