package com.example.fileshare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fileshare.R;
import com.example.fileshare.response.Notification;

import java.util.ArrayList;

public class StudentNotificationAdapter extends RecyclerView.Adapter<StudentNotificationAdapter.ViewHolder> {

    Context context;
    ArrayList<Notification> list;

    public StudentNotificationAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.notificationText.setText(list.get(i).getNotificationText());
        viewHolder.time.setText(list.get(i).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView notificationText, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationText = itemView.findViewById(R.id.student_notifications_textview_item);
            time = itemView.findViewById(R.id.student_notifications_time_textview_item);
        }
    }
}
