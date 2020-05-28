package com.example.passwordmanager.LoginDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.passwordmanager.Login;
import com.example.passwordmanager.LoginDatabase.LoginDbSchema.*;

import java.util.Date;
import java.util.UUID;

public class LoginCursorWrapper extends CursorWrapper {
    public LoginCursorWrapper(Cursor cursor) { super(cursor); }

    public Login getLogin() {
        String uuidString = getString(getColumnIndex(LoginTable.Cols.UUID));
        String username = getString(getColumnIndex(LoginTable.Cols.USERNAME));
        String passWord = getString(getColumnIndex(LoginTable.Cols.PASSWORD));
        long salt = getLong(getColumnIndex(LoginTable.Cols.SALT));

        Login login = new Login(UUID.fromString(uuidString));
        login.setUsername(username);
        login.setPassword(passWord);
        login.setSalt(new Date(salt));
        return login;
    }
}
