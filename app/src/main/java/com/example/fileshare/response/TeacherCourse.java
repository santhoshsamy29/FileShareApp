package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

public class TeacherCourse {

    @SerializedName("id")
    private int id;

    @SerializedName("department")
    private String department;

    @SerializedName("section")
    private String section;

    @SerializedName("course_year")
    private String courseYear;

    @SerializedName("course_name")
    private String courseName;

    @SerializedName("teacher_id")
    private int teacherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
}
