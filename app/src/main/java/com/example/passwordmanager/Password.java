package com.example.passwordmanager;

import java.util.Date;
import java.util.UUID;

public class Password {
    private UUID mId;
    private String mUsername;
    private String mPassword;
    private String mService;
    private int mLength;
    private int mIteration;
    private Date mDate;
    private boolean mSymbolToggle = false;

    public Password() {
        this(UUID.randomUUID());
    }

    public Password(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() { return mId; }

    public String getUsername() { return mUsername; }

    public void setUsername(String username) { mUsername = username; }

    public String getPassword() { return mPassword; }

    public void setPassword(String password) { mPassword = password; }

    public String getService() { return mService; }

    public void setService(String service) { mService = service; }

    public Date getDate() { return mDate; }

    public void setDate(Date date) { mDate = date; }

    public int getLength() { return mLength; }

    public void setLength(String size) {
        mLength = Integer.parseInt(size);
    }

    public int getIteration() { return mIteration; }

    public void setIteration(String newVersion) {
        try {
            mIteration = Integer.parseInt(newVersion);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public boolean getSymbolToggle() { return mSymbolToggle; }

    public void setSymbolToggle() {
        if (mSymbolToggle == false) {
            mSymbolToggle = true;
        } else {
            mSymbolToggle = false;
        }
    }
}
