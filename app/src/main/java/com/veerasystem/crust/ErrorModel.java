package com.veerasystem.crust;

import com.google.gson.annotations.SerializedName;

public class ErrorModel {
    @SerializedName("error")
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
