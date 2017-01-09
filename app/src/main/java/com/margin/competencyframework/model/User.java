package com.margin.competencyframework.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Martha on 1/8/2017.
 */
public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("firstname")
    private String mFirstName;
    @SerializedName("lastname")
    private String mLastName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mpassword;
    @SerializedName("id")
    private String mId;

    public User() {
    }

    protected User(Parcel in) {
        mFirstName = in.readString();
        mLastName = in.readString();
        mEmail = in.readString();
        mpassword = in.readString();
        mId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
        dest.writeString(mpassword);
        dest.writeString(mId);
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getMpassword() {
        return mpassword;
    }

    public void setMpassword(String mpassword) {
        this.mpassword = mpassword;
    }

    public String getId() {
        return mId;
    }
}
