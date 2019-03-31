package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

public class Enrollment {

    @SerializedName("course_id")
    private int courseId;

    @SerializedName("teacher_name")
    private String teacherName;

    @SerializedName("course_name")
    private String courseName;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
