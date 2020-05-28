package com.example.passwordmanager;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class PasswordActivity extends SingleFragmentActivity {
    private static final String EXTRA_PASSWORD_ID = "com.example.android.passwordintent.password_id";
    private static final String EXTRA_USER_ID = "com.example.android.passwordintent.user_id";
    private static final String EXTRA_USER_PASSWORD = "com.example.android.passwordintent.user_password_id";

    @Override
    protected Fragment createFragment() {
        UUID passwordId = (UUID) getIntent().getSerializableExtra(EXTRA_PASSWORD_ID);
        String userId = (String) getIntent().getSerializableExtra(EXTRA_USER_ID);
        String userPassword = getIntent().getStringExtra(EXTRA_USER_PASSWORD);
        return PasswordFragment.newInstance(passwordId, userId, userPassword);
    }
}
