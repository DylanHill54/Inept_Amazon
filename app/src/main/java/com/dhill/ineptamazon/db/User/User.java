package com.dhill.ineptamazon.db.User;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = DataBase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;



    private boolean misAdmin;

    private String mPassword;

    public User(String mUserName, String mPassword, String isAdmin) {
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        if(isAdmin.equals("yes")){
            setMisAdmin(true);
        }
    }
    public User(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        setMisAdmin(false);
    }

    public User() {
        setMisAdmin(false);

    }





    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public boolean isMisAdmin() {
        return misAdmin;
    }

    public void setMisAdmin(boolean misAdmin) {
        this.misAdmin = misAdmin;
    }

    @Override
    public String toString() {
        String output;
        output= "UserId:" + mUserId + "\n";;
        output+="UserName: " + mUserName+ "\n";
        output+="Admin: " + misAdmin+ "\n";
        output+="Password: " + mPassword+ "\n";
        return output;
    }
}
