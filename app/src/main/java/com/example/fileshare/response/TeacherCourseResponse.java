package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherCourseResponse {

    @SerializedName("data")
    private List<TeacherCourse> courses;

    @SerializedName("status")
    private int status;

    public List<TeacherCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<TeacherCourse> courses) {
        this.courses = courses;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
