package com.example.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Date;
import java.util.UUID;

public class PasswordFragment extends Fragment {
    private static final String TAG = "PasswordFragment";

    private static final String ARG_PASSWORD_ID = "password_id";
    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_USER_PASSWORD = "user_password";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private Password mPassword;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mServiceField;
    private EditText mLengthField;
    private Button mSymbolButton;
    private Button mDateButton;
    private Button mGenerateButton;
    private Button mSaveButton;

    private String mUuid;
    private String mUserPassword;

    public static PasswordFragment newInstance(UUID passwordId, String userId, String userPassword) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PASSWORD_ID, passwordId);
        args.putString(ARG_USER_ID, userId);
        args.putString(ARG_USER_PASSWORD, userPassword);
        PasswordFragment fragment = new PasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");

        SQLiteDatabase.loadLibs(getActivity());
        UUID passwordId = (UUID) getArguments().getSerializable(ARG_PASSWORD_ID);
        mUuid = (String) getArguments().getSerializable(ARG_USER_ID);
        mUserPassword = (String) getArguments().getSerializable(ARG_USER_PASSWORD);

        //Log.d(TAG, "userPassword" + mUserPassword);
        mPassword = PasswordList.get(getActivity(), mUuid, mUserPassword).getPassword(passwordId);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d(TAG, "onPause called");
        PasswordList.get(getActivity(), mUuid, mUserPassword).updatePassword(mPassword);
        mUserPassword = "";
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_password, container, false);

        mUsernameField = (EditText) v.findViewById(R.id.password_username);
        mUsernameField.setText(mPassword.getUsername());
        mUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPassword.setUsername(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPasswordField = (EditText) v.findViewById(R.id.password_password);
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPassword.setPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPasswordField.setText(mPassword.getPassword());

        mServiceField = (EditText) v.findViewById(R.id.password_service);
        mServiceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPassword.setService(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mServiceField.setText(mPassword.getService());

        mLengthField = (EditText) v.findViewById(R.id.password_length);
        mLengthField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    mPassword.setLength(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLengthField.setText(String.valueOf(mPassword.getLength()));

        mDateButton = (Button) v.findViewById(R.id.password_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mPassword.getDate());
                dialog.setTargetFragment(PasswordFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        mDateButton.setEnabled(false);
        mDateButton.setVisibility(View.INVISIBLE);

        mSymbolButton = (Button) v.findViewById(R.id.password_symbols);
        mSymbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword.setSymbolToggle();
                if (mPassword.getSymbolToggle() == true) {
                    mSymbolButton.setText(getString(R.string.password_symbol_on));
                } else {
                    mSymbolButton.setText(getString(R.string.password_symbol_off));
                }
            }
        });
        if (mPassword.getSymbolToggle() == true) {
            mSymbolButton.setText(getString(R.string.password_symbol_on));
        } else {
            mSymbolButton.setText(getString(R.string.password_symbol_off));
        }

        mSaveButton = (Button) v.findViewById(R.id.password_save);
        mSaveButton.setEnabled(true);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordList.get(getActivity(), mUuid, mUserPassword).updatePassword(mPassword);
                Toast.makeText(getActivity(), "Password Saved", Toast.LENGTH_SHORT).show();
            }
        });

        mGenerateButton = (Button) v.findViewById(R.id.password_generate);
        mGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getUsername() == null) {
                    Toast.makeText(getActivity(), "Password username is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPassword.getService() == null) {
                    Toast.makeText(getActivity(), "Password service is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if (mPassword.getLength() == null) {
                    Toast.makeText(getActivity(), "Password length set to 18", Toast.LENGTH_SHORT).show();
                    mPassword.setLength("18");
                    return;
                }*/
                String generatedPassword;
                String seedString;
                long seed = 0;

                seedString = mPassword.getUsername() + mPassword.getIteration() + mPassword.getService() + mUserPassword;
                char[] seedArray = seedString.toCharArray();
                Character[] charArray = new Character[seedString.length()];
                for (int i = 0; i < seedString.length(); i++) {
                    charArray[i] = seedArray[i];
                }

                for (int i = 0; i < seedString.length(); i++) {
                    seed += Character.getNumericValue(charArray[i]); // + Date created + iteration
                }

                StringGenerator sg = new StringGenerator();
                sg.setUseSymbols(mPassword.getSymbolToggle());
                generatedPassword = sg.generatedString(seed, mPassword.getLength());
                if (generatedPassword == "failed")
                {
                    Toast.makeText(getActivity(), "String length too short. Has to be at least 8 characters.", Toast.LENGTH_SHORT).show();
                } else {
                    mPassword.setPassword(generatedPassword);
                    mPasswordField.setText(mPassword.getPassword());
                    int newVersion = mPassword.getIteration() + 1;
                    mPassword.setIteration(String.valueOf(newVersion));
                    //Toast.makeText(getActivity(), String.valueOf(mPassword.getIteration()), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(), generatedPassword, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mPassword.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mPassword.getDate().toString());
    }
}
