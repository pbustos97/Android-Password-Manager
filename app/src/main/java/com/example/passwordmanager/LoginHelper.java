package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.passwordmanager.LoginDatabase.LoginBaseHelper;
import com.example.passwordmanager.LoginDatabase.LoginCursorWrapper;
import com.example.passwordmanager.LoginDatabase.LoginDbSchema.*;

public class LoginHelper {
    private static String TAG = "LoginHelper";

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public LoginHelper(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LoginBaseHelper(mContext).getWritableDatabase();
    }

    // Returns login from database
    public Login getLogin(String userName) {
        LoginCursorWrapper cursor = queryLogins(
                LoginTable.Cols.USERNAME + " = ?",
                new String[] { userName }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getLogin();
        } finally {
            cursor.close();
        }
    }

    // Looks for login username
    private LoginCursorWrapper queryLogins(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                LoginTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new LoginCursorWrapper(cursor);
    }

    public void addLogin(Login l) {
        ContentValues values = getContentValues(l);
        mDatabase.insert(LoginTable.NAME, null, values);
    }

    // Use in future???
    public void updateLogin(Login l) {
        String uuidString = l.getId().toString();
        ContentValues values = getContentValues(l);

        mDatabase.update(LoginTable.NAME, values,
                LoginTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public int deleteLogin(Login l) {
        String uuidString = l.getId().toString();
        String username = l.getUsername();
        String password = l.getPassword();
        ContentValues values;

        if (username.length() == 0) {
            return 5;
        }

        if (password.length() == 0) {
            return 4;
        }

        Login exists;

        exists = getLogin(username);

        if (exists == null) {
            return 1;
        }

        l.setSalt(exists.getSalt());
        l.setPassword(l.hashedPassword(l.getPassword()));

        //Log.d(TAG, l.getPassword());
        //Log.d(TAG, exists.getPassword());

        uuidString = l.getId().toString();
        username = l.getUsername();
        password = l.getPassword();
        values = getContentValues(l);

        if (!exists.getPassword().equals(password)) {
            return 2;
        }

        //Log.d(TAG, "Passed password equality");

        if (mDatabase.delete(LoginTable.NAME,
                LoginTable.Cols.USERNAME + " = ?",
                new String[]{ username }) == 0) {
            return 3;
        }
        return 0;
    }

    private static ContentValues getContentValues(Login login) {
        ContentValues values = new ContentValues();
        values.put(LoginTable.Cols.UUID, login.getId().toString());
        values.put(LoginTable.Cols.USERNAME, login.getUsername());
        values.put(LoginTable.Cols.PASSWORD, login.getPassword());
        values.put(LoginTable.Cols.SALT, login.getSalt().getTime());
        return values;
    }

    public void close() {
        mDatabase.close();
    }
}
