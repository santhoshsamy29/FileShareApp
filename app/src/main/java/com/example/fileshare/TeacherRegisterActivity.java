package com.example.fileshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fileshare.response.StandardResponse;
import com.example.fileshare.retrofit.ApiClient;
import com.example.fileshare.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherRegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        initialize();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }



    private void initialize() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        registerButton = findViewById(R.id.teacher_register_button);
        nameEditText = findViewById(R.id.teacher_nameEditText);
        emailEditText = findViewById(R.id.teacher_emailEditText);
        passwordEditText = findViewById(R.id.teacher_passwordEditText);
        confirmPasswordEditText = findViewById(R.id.teacher_confirmPasswordEditText);
    }


    private void validate() {

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if(name.isEmpty() || name == ""){
            nameEditText.requestFocus();
            nameEditText.setError("Enter your name");
        } else if(email.isEmpty() || email == "") {
            emailEditText.requestFocus();
            emailEditText.setError("Email is required");
        } else if (password.isEmpty() || password == "") {
            passwordEditText.requestFocus();
            passwordEditText.setError("Password is required");
        } else if(confirmPassword.isEmpty() || confirmPassword == "") {
            confirmPasswordEditText.requestFocus();
            confirmPasswordEditText.setError("Confirm your password");
        } else {
            if(password.equals(confirmPassword)){
                register(name, email, password);
            } else {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void register(String name, String email, String password) {

        Call<StandardResponse> call = apiInterface.teacherRegister(name, email, password);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {

                String data = response.body() != null ? response.body().getData() : null;
                Toast.makeText(TeacherRegisterActivity.this, data, Toast.LENGTH_SHORT).show();

                clearEditTexts();
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                Toast.makeText(TeacherRegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearEditTexts() {
        nameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
    }

}
