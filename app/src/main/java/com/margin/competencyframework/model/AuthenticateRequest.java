package com.margin.competencyframework.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Martha on 1/8/2017.
 */
public class AuthenticateRequest {

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;
}
