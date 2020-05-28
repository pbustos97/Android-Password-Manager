package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordListFragment extends Fragment {
    private static final String TAG = "PasswordListFragment";

    private RecyclerView mPasswordRecyclerView;
    private PasswordAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private String mUuid = "";
    private String mUserPassword = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mUuid = bundle.getString(getString(R.string.uuid_key));
            mUserPassword = bundle.getString(getString(R.string.password_key));
        }
        try {
            Log.d(TAG, "onCreate " + mUuid);
            Log.d(TAG, "onCreate " + mUserPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_list, container, false);

        mPasswordRecyclerView = (RecyclerView) view
                .findViewById(R.id.password_recycler_view);
        mPasswordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        PasswordList passwordlist = PasswordList.get(getActivity(), mUuid, mUserPassword);
        passwordlist.openDb();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_password_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_password:
                Password password = new Password();
                PasswordList.get(getActivity(), mUuid, mUserPassword).addPassword(password);
                Intent intent = PasswordPagerActivity.newIntent(getActivity(), password.getId(), mUuid, mUserPassword);
                startActivityForResult(intent, 1);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        PasswordList passwordList = PasswordList.get(getActivity(), mUuid, mUserPassword);
        int passwordCount = passwordList.getPasswords().size();
        String subtitle = getString(R.string.subtitle_format, passwordCount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        PasswordList passwordList = PasswordList.get(getActivity(), mUuid, mUserPassword);
        List<Password> passwords = passwordList.getPasswords();

        if (mAdapter == null){
            mAdapter = new PasswordAdapter(passwords);
            mPasswordRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPasswords(passwords);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class PasswordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mUsernameTextView;
        private TextView mServiceTextView;
        private Button mDeleteButton;
        private Password mPassword;

        public PasswordHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_password, parent, false));
            itemView.setOnClickListener(this);

            mUsernameTextView = (TextView) itemView.findViewById(R.id.password_username);
            mServiceTextView = (TextView) itemView.findViewById(R.id.password_service);
            mDeleteButton = (Button) itemView.findViewById(R.id.password_delete);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PasswordList.get(getActivity(), mUuid, mUserPassword).deletePassword(mPassword);
                    updateUI();
                }
            });

        }

        public void bind(Password password) {
            mPassword = password;
            mUsernameTextView.setText(mPassword.getUsername());
            mServiceTextView.setText(mPassword.getService());
            mDeleteButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = PasswordPagerActivity.newIntent(getActivity(), mPassword.getId(), mUuid, mUserPassword);
            startActivityForResult(intent, 1);
        }
    }

    private class PasswordAdapter extends RecyclerView.Adapter<PasswordHolder> {
        private List<Password> mPasswords;

        public PasswordAdapter(List<Password> passwords) {
            mPasswords = passwords;
        }

        @Override
        public PasswordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new PasswordHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PasswordHolder holder, int position) {
            Password password = mPasswords.get(position);
            holder.bind(password);
        }

        @Override
        public int getItemCount() {
            return mPasswords.size();
        }

        public void setPasswords(List<Password> passwords) {
            mPasswords = passwords;
        }
    }

    public void close() {
        PasswordList passwordlist = PasswordList.get(getActivity(), mUuid, mUserPassword);
        passwordlist.closeDb();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    // Gets data from PasswordPagerActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult called");
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                mUuid = intent.getStringExtra(getString(R.string.uuid_key));
                mUserPassword = intent.getStringExtra(getString(R.string.password_key));
            }
        }
    }

    /*
    public void onBackPressed() {
        PasswordList passwordlist = PasswordList.get(getActivity(), mUuid, mUserPassword);
        passwordlist.closeDb();
        mUserPassword = "";
    }*/
}
