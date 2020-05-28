package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class PasswordPagerActivity extends AppCompatActivity {
    private static final String TAG = "PasswordPagerActivity";

    private static final String EXTRA_PASSWORD_ID = "com.example.android.passwordintent.password_id";
    private static final String EXTRA_UUID_KEY_ID = "com.example.android.passwordintent.uuid_key";
    private static final String EXTRA_USER_PASSWORD = "com.example.android.passwordintent.password_key";

    private ViewPager mViewPager;
    private List<Password> mPasswords;
    private String mUuid;
    private String mUserPassword;

    public static Intent newIntent(Context packageContext, UUID passwordId, String uuid, String userPassword) {
        Intent intent = new Intent(packageContext, PasswordPagerActivity.class);
        intent.putExtra(EXTRA_PASSWORD_ID, passwordId);
        intent.putExtra(EXTRA_UUID_KEY_ID, uuid);
        intent.putExtra(EXTRA_USER_PASSWORD, userPassword);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_password_pager);

        if (savedInstanceBundle != null) {
            setId(savedInstanceBundle);
        }

        UUID passwordId = (UUID) getIntent().getSerializableExtra(EXTRA_PASSWORD_ID);
        mUuid = (String) getIntent().getSerializableExtra(EXTRA_UUID_KEY_ID);
        mUserPassword = (String) getIntent().getStringExtra(EXTRA_USER_PASSWORD);

        //Log.d(TAG, mUuid);
        //Log.d(TAG, "userPassword " + mUserPassword);

        mViewPager = (ViewPager) findViewById(R.id.password_view_pager);

        mPasswords = PasswordList.get(this, mUuid, mUserPassword).getPasswords();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Password password = mPasswords.get(position);
                return PasswordFragment.newInstance(password.getId(), mUuid, mUserPassword);
            }

            @Override
            public int getCount() {
                return mPasswords.size();
            }
        });

        for (int i = 0; i < mPasswords.size(); i++) {
            if (mPasswords.get(i).getId().equals(passwordId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
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

    // Returns result to PasswordListFragment
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.uuid_key), mUuid);
        intent.putExtra(getString(R.string.password_key), mUserPassword);
        mUserPassword = "";
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setId(Bundle bundle) {
        Log.d(TAG, bundle.toString());
        mUuid = bundle.getString(getString(R.string.uuid_key));
    }
}
