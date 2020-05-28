package com.example.passwordmanager.database;


import com.example.passwordmanager.Password;

import net.sqlcipher.Cursor;
import net.sqlcipher.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import static com.example.passwordmanager.database.PasswordDbSchema.*;

public class PasswordCursorWrapper extends CursorWrapper {
    public PasswordCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Password getPassword() {
        String uuidString = getString(getColumnIndex(PasswordTable.Cols.UUID));
        String service = getString(getColumnIndex(PasswordTable.Cols.SERVICE));
        String username = getString(getColumnIndex(PasswordTable.Cols.USERNAME));
        String passWord = getString(getColumnIndex(PasswordTable.Cols.PASSWORD));
        String length = getString(getColumnIndex(PasswordTable.Cols.LENGTH));
        String version = getString(getColumnIndex(PasswordTable.Cols.VERSION));
        long date = getLong(getColumnIndex(PasswordTable.Cols.DATE));

        Password password = new Password(UUID.fromString(uuidString));
        password.setService(service);
        password.setUsername(username);
        password.setPassword(passWord);
        password.setDate(new Date(date));
        password.setLength(length);
        password.setIteration(version);
        return password;
    }
}
