package com.example.fileshare.retrofit;

import com.example.fileshare.response.CourseResponse;
import com.example.fileshare.response.EnrollmentResponse;
import com.example.fileshare.response.FileResponse;
import com.example.fileshare.response.StandardResponse;
import com.example.fileshare.response.TeacherCourseResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiInterface {

    //Teacher registration and login
    @FormUrlEncoded
    @POST("/api/teacher/register")
    Call<StandardResponse> teacherRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/api/teacher/login")
    Call<StandardResponse> teacherLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/api/teacher/course")
    Call<TeacherCourseResponse> getCourseFromTeacherId(
            @Field("teacher_id") int teacherId
    );

    //Student registration and login
    @FormUrlEncoded
    @POST("/api/student/register")
    Call<StandardResponse> studentRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("/api/student/login")
    Call<StandardResponse> studentLogin(
            @Field("email") String email,
            @Field("password") String password
    );


    //Enrollment
    @FormUrlEncoded
    @POST("/api/enroll")
    Call<StandardResponse> enroll(
            @Field("student_id") Integer studentId,
            @Field("course_id") Integer courseId
    );

    @FormUrlEncoded
    @POST("/api/enroll/display")
    Call<EnrollmentResponse> getEnrollment(
            @Field("student_id") Integer studentId
    );


    //File upload and fetch
    @Multipart
    @POST("/api/file/upload")
    Call<StandardResponse> upload(
            @Part MultipartBody.Part file,
            @Part("course_id") RequestBody courseId
    );

    @FormUrlEncoded
    @POST("/api/file/show")
    Call<FileResponse> getFiles(
            @Field("course_id") Integer courseId
    );


    @GET("/api/course/show")
    Call<CourseResponse> getCourses();

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String urlString);

}
