package com.huypo.tase.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respone {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("token")
    @Expose
    private String token;

    public Boolean getMessage() {
        return result;
    }

    public String getToken() {
        return token;
    }
}
