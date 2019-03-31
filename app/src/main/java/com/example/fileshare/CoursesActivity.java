package com.example.fileshare;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.fileshare.adapter.CoursesAdapter;
import com.example.fileshare.response.Course;
import com.example.fileshare.response.CourseResponse;
import com.example.fileshare.response.StandardResponse;
import com.example.fileshare.retrofit.ApiClient;
import com.example.fileshare.retrofit.ApiInterface;
import com.example.fileshare.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesActivity extends AppCompatActivity {

    RecyclerView coursesRecyclerView;
    CoursesAdapter coursesAdapter;

    ArrayList<Course> list = new ArrayList<>();

    ApiInterface apiInterface;
    int studentId = 1;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        initialize();
        preferences = getApplicationContext().getSharedPreferences(Constants.LOGIN_PREF, 0);
        studentId = preferences.getInt(Constants.STUDENT_ID, 1);


        fetchCourses();

        coursesAdapter.setOnClickListener(new CoursesAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                enroll(position);
            }
        });
    }

    private void enroll(int courseId) {

        Call<StandardResponse> call = apiInterface.enroll(studentId, courseId);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(CoursesActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {

            }
        });
    }

    private void fetchCourses() {

        Call<CourseResponse> call = apiInterface.getCourses();
        call.enqueue(new Callback<CourseResponse>() {
            @Override
            public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                List<Course> courses = response.body().getCourses();

                for(int i=0; i<courses.size() ; i++){
                    list.add(courses.get(i));
                }

                coursesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CourseResponse> call, Throwable t) {

            }
        });

    }

    private void initialize() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        coursesAdapter = new CoursesAdapter(this, list);
        coursesRecyclerView = findViewById(R.id.courses_recyclerview);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        coursesRecyclerView.setAdapter(coursesAdapter);
    }


}
