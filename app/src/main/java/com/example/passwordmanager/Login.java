package com.example.passwordmanager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

// Stores user's login information
public class Login {
    private UUID mId;
    private String mUsername;
    private String mPassword;
    private Date mSalt;

    public Login() { this(UUID.randomUUID()); }

    public Login(UUID uuid) {
        mId = uuid;
        mSalt = new Date();
    }

    public UUID getId() { return mId; }

    public String getUsername() { return mUsername; }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() { return mPassword; }

    public void setPassword(String password) {
        mPassword = password;
    }

    public Date getSalt() { return mSalt; }

    public void setSalt(Date date) {
        mSalt = date;
    }

    // Hashes the password using SHA-256 with salt
    public String hashedPassword(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((s + mSalt.toString()).getBytes());
            byte[] digest = md.digest();
            StringBuffer hex = new StringBuffer();

            for (int i = 0; i < digest.length; i++) {
                hex.append(Integer.toHexString(0XFF + digest[i]));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "Failed to generated the salted and hashed password";
    }
}
