package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

public class StandardResponse {

    @SerializedName("data")
    private String data;

    @SerializedName("status")
    private Integer status;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
