package com.example.passwordmanager.database;

import com.example.passwordmanager.SingleFragmentActivity;

public class PasswordDbSchema {
    public static final class PasswordTable {
        public static final String NAME = "passwords";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String SERVICE = "service";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String LENGTH = "length";
            public static final String VERSION = "version";
            public static final String DATE = "date";
        }
    }
}
