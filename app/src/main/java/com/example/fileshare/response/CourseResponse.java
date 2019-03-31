package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseResponse {

    @SerializedName("data")
    private List<Course> courses;

    @SerializedName("status")
    private int status;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
