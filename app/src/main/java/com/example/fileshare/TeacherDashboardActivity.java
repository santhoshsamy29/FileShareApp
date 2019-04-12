package com.example.fileshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fileshare.adapter.TeacherDashboardAdapter;
import com.example.fileshare.response.StandardResponse;
import com.example.fileshare.response.TeacherCourse;
import com.example.fileshare.response.TeacherCourseResponse;
import com.example.fileshare.retrofit.ApiClient;
import com.example.fileshare.retrofit.ApiInterface;
import com.example.fileshare.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherDashboardActivity extends AppCompatActivity {

    RecyclerView teacherDashboardRecyclerView;
    TeacherDashboardAdapter teacherDashboardAdapter;
    ArrayList<TeacherCourse> list = new ArrayList<>();

    AlertDialog dialog;
    EditText dialogNotificationTextEditText;

    ApiInterface apiInterface;
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    int teacherId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);


        initialize();
        pref = getApplicationContext().getSharedPreferences(Constants.LOGIN_PREF, 0);
        edit = pref.edit();

        teacherId = pref.getInt(Constants.TEACHER_ID, 1);

        fetchCourses();

        teacherDashboardAdapter.setOnClickListener(new TeacherDashboardAdapter.OnClickListener() {

            @Override
            public void notificationsClicked(View view, final int courseId) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TeacherDashboardActivity.this);
                LayoutInflater inflater = TeacherDashboardActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.notification_dialog, null);
                dialogBuilder.setView(dialogView);
                dialog = dialogBuilder.create();
                dialog.show();

                dialogNotificationTextEditText = dialogView.findViewById(R.id.send_notification_edittext);
                Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
                Button sendButton = dialogView.findViewById(R.id.dialog_send_button);

                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendNotification(courseId);
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void itemClicked(View view, int courseId) {
                Intent intent = new Intent(TeacherDashboardActivity.this, TeacherFileListActivity.class);
                intent.putExtra(Constants.COURSE_ID, courseId);
                startActivity(intent);
            }
        });
    }

    private void sendNotification(int courseId) {

        String notification = dialogNotificationTextEditText.getText().toString().trim();

        if(notification.isEmpty()){
            Toast.makeText(this, "Enter something", Toast.LENGTH_SHORT).show();
        } else {
            sendNotificationToServer(courseId, notification);
        }
    }

    private void sendNotificationToServer(int courseId, String notificationText) {

        Call<StandardResponse> call = apiInterface.sendNotifications(courseId, notificationText);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if(response.body().getStatus() == 200){
                    Toast.makeText(TeacherDashboardActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(TeacherDashboardActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                Toast.makeText(TeacherDashboardActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {

        teacherDashboardAdapter = new TeacherDashboardAdapter(this, list);
        teacherDashboardRecyclerView = findViewById(R.id.teacher_dashboard_recyclerview);
        teacherDashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacherDashboardRecyclerView.setAdapter(teacherDashboardAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }


    private void fetchCourses() {


        Call<TeacherCourseResponse> call = apiInterface.getCourseFromTeacherId(teacherId);
        call.enqueue(new Callback<TeacherCourseResponse>() {
            @Override
            public void onResponse(Call<TeacherCourseResponse> call, Response<TeacherCourseResponse> response) {

                List<TeacherCourse> temp = response.body().getCourses();
                Log.e("SAN", list.size() + "");

                for(int i=0; i<temp.size() ; i++){
                    list.add(temp.get(i));
                }

                teacherDashboardAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<TeacherCourseResponse> call, Throwable t) {

            }
        });
    }
}
