package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

public class Course {

    @SerializedName("course_id")
    private int courseId;

    @SerializedName("course_name")
    private String courseName;

    @SerializedName("teacher_name")
    private String teacherName;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
