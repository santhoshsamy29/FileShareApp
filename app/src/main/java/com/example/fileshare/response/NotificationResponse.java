package com.example.fileshare.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {

    @SerializedName("data")
    private List<Notification> notifications;

    @SerializedName("status")
    private int status;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
