package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

public class CodeForQRImage {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
