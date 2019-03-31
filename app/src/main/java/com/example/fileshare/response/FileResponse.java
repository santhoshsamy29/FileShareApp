package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileResponse {

    @SerializedName("data")
    private List<File> files;

    @SerializedName("status")
    private int status;

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
