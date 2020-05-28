package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Date;

public class LoginFragment extends Fragment{
    private static final String TAG = "Login Fragment";
    private String mUserId;

    private EditText mUsernameInput;
    private EditText mPasswordInput;
    private Button mLoginButton;
    private Button mRegisterButton;
    private Button mDeleteButton;

    private LoginHelper mLoginDb;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginDb = new LoginHelper(getActivity());

        mUsernameInput = (EditText) view.findViewById(R.id.input_Username);
        mPasswordInput = (EditText) view.findViewById(R.id.input_Password);

        mLoginButton = (Button) view.findViewById(R.id.button_login);
        mRegisterButton = (Button) view.findViewById(R.id.button_register);
        mDeleteButton = (Button) view.findViewById(R.id.button_delete);


        // Moves to new activity when user enters username and password perfectly
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsernameInput.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(),
                            R.string.register_username_length,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPasswordInput.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(),
                            R.string.register_password_length,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if login is inside loginBase.db (done)
                // If not, send toast (done). Else change activity (done)
                Login exists = mLoginDb.getLogin(mUsernameInput.getText().toString());

                if (exists == null) {
                    Toast.makeText(getActivity(),
                            R.string.delete_no_user,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Login login = new Login();
                    login.setUsername(mUsernameInput.getText().toString());
                    login.setPassword(mPasswordInput.getText().toString());
                    login.setSalt(exists.getSalt());

                    login.setPassword(login.hashedPassword(login.getPassword()));

                    if (exists.getPassword().equals(login.getPassword())) {
                        Toast.makeText(getActivity(),
                                R.string.login_successful,
                                Toast.LENGTH_SHORT).show();
                        // Move to PasswordPagerActivity intent
                        // Load 'uuid'Db.db
                        mUserId = exists.getId().toString();
                        //Log.d(TAG, "login " + mUserId);
                        Bundle bundle = new Bundle();
                        Intent intent = PasswordListActivity.newIntent(getActivity());
                        setBundles(bundle, intent);

                        startActivity(intent, bundle);

                    } else {
                        throw new Exception();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            R.string.login_failed,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Moves to new activity if username does not exist and all fields have text
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if username is in loginBase.db (done)
                // If in loginBase.db send toast error (done)

                // Save username, password, date, and new UUID into new Login class (done)
                // Add to loginBase.db (done)
                try {
                    Login login = new Login();
                    login.setSalt(new Date());
                    login.setUsername(mUsernameInput.getText().toString());
                    login.setPassword(login.hashedPassword(mPasswordInput.getText().toString()));

                    if (mLoginDb.getLogin(login.getUsername()) == null && mPasswordInput.getText().toString().length() != 0) {
                        mLoginDb.addLogin(login);
                        mUserId = login.getId().toString();
                        //Log.d(TAG, "register " + mUserId);
                        Bundle bundle = new Bundle();
                        Intent intent = PasswordListActivity.newIntent(getActivity());
                        setBundles(bundle, intent);

                        startActivity(intent, bundle);

                        Toast.makeText(getActivity(),
                                R.string.register_successful,
                                Toast.LENGTH_SHORT).show();
                    } else if (mUsernameInput.getText().toString().length() == 0) {
                        Toast.makeText(getActivity(),
                                R.string.register_username_length,
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (mPasswordInput.getText().toString().length() == 0) {
                        Toast.makeText(getActivity(),
                                R.string.register_password_length,
                                Toast.LENGTH_SHORT).show();
                    } else if (mLoginDb.getLogin(login.getUsername()).getUsername() != null) {
                        Toast.makeText(getActivity(),
                                R.string.register_exists,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            R.string.register_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete user if username and password match
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete loginBase.db entry if username and password matches database entry (done)
                // Send toast if success or fail (done)
                // Delete 'uuid'Db.db(done)

                Login login = new Login();
                login.setUsername(mUsernameInput.getText().toString());
                login.setPassword(mPasswordInput.getText().toString());

                // Set mUserId so that login or register entries are not deleted from device
                try {
                    Login exists = mLoginDb.getLogin(mUsernameInput.getText().toString());
                    mUserId = exists.getId().toString();

                    // exists not necessary anymore
                    exists = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 0 = Deleted user
                // others = errors
                switch (mLoginDb.deleteLogin(login)) {
                    case 0:
                        PasswordList.get(getActivity(), mUserId, mPasswordInput.getText().toString()).closeDb();
                        getActivity().deleteDatabase(mUserId + ".db");

                        Toast.makeText(getActivity(),
                                R.string.delete_success,
                                Toast.LENGTH_SHORT).show();
                        return;
                    case 1:
                    case 3:
                        Toast.makeText(getActivity(),
                                R.string.delete_no_user,
                                Toast.LENGTH_SHORT).show();
                        return;
                    case 2:
                        Toast.makeText(getActivity(),
                                R.string.delete_wrong_password,
                                Toast.LENGTH_SHORT).show();
                        return;
                    case 4:
                        Toast.makeText(getActivity(),
                                R.string.register_password_length,
                                Toast.LENGTH_SHORT).show();
                        return;
                    case 5:
                        Toast.makeText(getActivity(),
                                R.string.register_username_length,
                                Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        Toast.makeText(getActivity(),
                                R.string.delete_error,
                                Toast.LENGTH_SHORT).show();
                        return;
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    // Reopens database when user comes back to start page
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        mLoginDb = new LoginHelper(getActivity());
    }

    // Close database when user moves to new activity
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        mLoginDb.close();
    }

    // Close database when no longer needed
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        mLoginDb.close();
    }

    // Close database when user removes app from background tasks
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
        mLoginDb.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // Bundle needed to pass data to deeper fragments
    private void setBundles(Bundle bundle, Intent intent) {
        bundle.putString(getString(R.string.uuid_key), mUserId);
        bundle.putString(getString(R.string.password_key), mPasswordInput.getText().toString());
        intent.putExtra(getString(R.string.uuid_key), mUserId);
        intent.putExtra(getString(R.string.password_key), mPasswordInput.getText().toString());
    }
}
