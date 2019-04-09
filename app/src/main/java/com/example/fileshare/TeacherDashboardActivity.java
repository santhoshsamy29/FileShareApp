package com.example.fileshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.fileshare.adapter.TeacherDashboardAdapter;
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
            public void itemClicked(View view, int courseId) {
                Intent intent = new Intent(TeacherDashboardActivity.this, TeacherFileListActivity.class);
                intent.putExtra(Constants.COURSE_ID, courseId);
                startActivity(intent);
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
