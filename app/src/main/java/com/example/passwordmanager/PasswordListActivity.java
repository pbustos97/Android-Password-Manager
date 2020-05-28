package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

public class PasswordListActivity extends SingleFragmentActivity {
    private static final String TAG = "PasswordListActivity";

    @Override
    protected Fragment createFragment() {
        return new PasswordListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d(TAG, "createFragment ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, PasswordListActivity.class);
        return intent;
    }
}
