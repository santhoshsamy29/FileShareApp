package com.example.fileshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fileshare.adapter.StudentDashboardAdapter;
import com.example.fileshare.response.Enrollment;
import com.example.fileshare.response.EnrollmentResponse;
import com.example.fileshare.retrofit.ApiClient;
import com.example.fileshare.retrofit.ApiInterface;
import com.example.fileshare.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDashboardActivity extends AppCompatActivity {

    RecyclerView studentCoursesRecyclerView;
    ArrayList<Enrollment> list = new ArrayList<>();

  //  ArrayList<String> list = new ArrayList<>();

    ApiInterface apiInterface;
    StudentDashboardAdapter studentDashboardAdapter;

    int studentId = 1;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);


        initialize();
        pref = getApplicationContext().getSharedPreferences(Constants.LOGIN_PREF, 0);
        studentId = pref.getInt(Constants.STUDENT_ID, 1);

        //fetchCourses();

        studentDashboardAdapter.setOnClickListener(new StudentDashboardAdapter.OnClickListener() {

            @Override
            public void notificationsClicked(View view, int position) {

                Intent intent = new Intent(StudentDashboardActivity.this, StudentNotificationsActivity.class);
                intent.putExtra(Constants.COURSE_ID,position);
                startActivity(intent);

            }

            @Override
            public void itemClicked(View view, int position) {
                Log.e("SAN", "filess clickedsdsds");
                Intent intent = new Intent(StudentDashboardActivity.this, StudentFileListActivity.class);
                intent.putExtra(Constants.COURSE_ID,position);
                startActivity(intent);
            }
        });
    }

    private void initialize() {

        studentCoursesRecyclerView = findViewById(R.id.student_courses_recyclerview);
        studentCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentDashboardAdapter = new StudentDashboardAdapter(this, list);
        studentCoursesRecyclerView.setAdapter(studentDashboardAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    private void fetchCourses() {



        Call<EnrollmentResponse> call = apiInterface.getEnrollment(studentId);
        call.enqueue(new Callback<EnrollmentResponse>() {
            @Override
            public void onResponse(Call<EnrollmentResponse> call, Response<EnrollmentResponse> response) {
                if (response.body() != null) {
                    List<Enrollment> enrollments = response.body().getEnrollments();



                    list.clear();

                    for(int i=0; i<enrollments.size() ; i++){

                        Log.e("SAN", "coruse  : " + enrollments.get(i).getCourseName() + "   "  + enrollments.get(i).getTeacherName());
                        list.add(enrollments.get(i));
                        //list.add(enrollments.get(i).getCourseName());

                    }

                    studentDashboardAdapter.notifyDataSetChanged();
                    Toast.makeText(StudentDashboardActivity.this, "Loaded", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<EnrollmentResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_enroll) {

            startActivity(new Intent(this, CoursesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchCourses();
    }
}
