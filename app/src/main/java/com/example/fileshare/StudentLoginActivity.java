package com.example.fileshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fileshare.response.StandardResponse;
import com.example.fileshare.retrofit.ApiClient;
import com.example.fileshare.retrofit.ApiInterface;
import com.example.fileshare.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentLoginActivity extends AppCompatActivity {


    TextView registerTextView;
    EditText emailEditText, passwordEditText;
    Button loginButton;
    ApiInterface apiInterface;

    SharedPreferences pref ;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);



        initialize();
        pref = getApplicationContext().getSharedPreferences(Constants.LOGIN_PREF, 0);
        edit = pref.edit();

        if(pref.getBoolean(Constants.STUDENT_LOGGED_IN, false)){
            startActivity(new Intent(this, StudentDashboardActivity.class));
            finish();
        }

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentLoginActivity.this, StudentRegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void initialize() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        loginButton = findViewById(R.id.student_login_button);
        registerTextView = findViewById(R.id.student_register_textview);
        emailEditText = findViewById(R.id.student_emailEditText);
        passwordEditText = findViewById(R.id.student_passwordEditText);
    }

    private void validate() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(email.isEmpty() || email == "") {
            emailEditText.requestFocus();
            emailEditText.setError("Email is required");
        } else if (password.isEmpty() || password == "") {
            passwordEditText.requestFocus();
            passwordEditText.setError("Password is required");
        } else {
            login(email, password);
        }
    }

    private void login(String email, String password) {
        Call<StandardResponse> call = apiInterface.studentLogin(email, password);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {

                Log.e("SAN","response");
                int status = response.body().getStatus();
                String data = response.body().getData();

                if (status == 200){
                    int studentId = Integer.valueOf(data);
                    edit.putInt(Constants.STUDENT_ID, studentId);
                    edit.putBoolean(Constants.STUDENT_LOGGED_IN, true);
                    edit.apply();
                    startActivity(new Intent(StudentLoginActivity.this, StudentDashboardActivity.class));
                    finish();
                    clearEditTexts();
                } else {
                    Toast.makeText(StudentLoginActivity.this, data , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                Log.e("SAN", "failed : " + t.getMessage());
            }
        });
    }

    private void clearEditTexts() {
        emailEditText.setText("");
        passwordEditText.setText("");
    }
}
