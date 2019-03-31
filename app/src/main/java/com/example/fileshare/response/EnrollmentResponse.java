package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnrollmentResponse {

    @SerializedName("data")
    private List<Enrollment> enrollments;

    @SerializedName("status")
    private int status;

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
