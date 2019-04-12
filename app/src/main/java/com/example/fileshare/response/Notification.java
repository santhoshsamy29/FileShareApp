package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("id")
    private int id;

    @SerializedName("notification_text")
    private String notificationText;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("course_id")
    private int courseId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
