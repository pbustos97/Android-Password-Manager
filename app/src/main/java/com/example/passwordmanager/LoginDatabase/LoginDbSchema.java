package com.example.passwordmanager.LoginDatabase;

public class LoginDbSchema {
    public static final class LoginTable {
        public static final String NAME = "logins";

        public static final class Cols {
            public static final String UUID = "uuid"; // Do not need???
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String SALT = "date";
        }
    }
}
