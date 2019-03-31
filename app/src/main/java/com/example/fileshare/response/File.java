package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("sise")
    private String size;

    @SerializedName("course_id")
    private int courseId;

    @SerializedName("created_at")
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
