package com.example.fileshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.fileshare.adapter.StudentNotificationAdapter;
import com.example.fileshare.response.Notification;
import com.example.fileshare.response.NotificationResponse;
import com.example.fileshare.response.StandardResponse;
import com.example.fileshare.retrofit.ApiClient;
import com.example.fileshare.retrofit.ApiInterface;
import com.example.fileshare.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentNotificationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ApiInterface apiInterface;
    int courseId = 1;

    ArrayList<Notification> list = new ArrayList<>();
    StudentNotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notifications);

        initialize();

        fetchNotifications();

    }

    private void fetchNotifications() {

        Call<NotificationResponse> call = apiInterface.getNotifications(courseId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                if(response.body().getStatus() == 200){
                    list.clear();
                    List<Notification> temp = response.body().getNotifications();
                    list.addAll(temp);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(StudentNotificationsActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Toast.makeText(StudentNotificationsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {

        courseId = getIntent().getIntExtra(Constants.COURSE_ID, 1);

        adapter = new StudentNotificationAdapter(this, list);
        recyclerView = findViewById(R.id.student_notifications_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

    }


}
