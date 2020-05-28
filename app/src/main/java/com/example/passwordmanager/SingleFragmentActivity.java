package com.example.passwordmanager;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    private static final String TAG = "SingleFragmentActivity";

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            Bundle bundle = new Bundle();
            try {
                String mUserId = getIntent().getStringExtra(getString(R.string.uuid_key));
                String mUserPassword = getIntent().getStringExtra(getString(R.string.password_key));
                if (mUserId == null) {
                    throw new Exception();
                }
                bundle.putString(getString(R.string.uuid_key), mUserId);
                bundle.putString(getString(R.string.password_key), mUserPassword);

                Log.d("SFA", mUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragment = createFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        } else {
            Log.d(TAG, "Fragment not null");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }
}
