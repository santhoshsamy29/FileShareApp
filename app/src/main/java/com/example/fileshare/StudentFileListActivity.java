package com.example.fileshare;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.example.fileshare.adapter.StudentFileListAdapter;
import com.example.fileshare.adapter.TeacherFileListAdapter;
import com.example.fileshare.response.FileResponse;
import com.example.fileshare.retrofit.ApiClient;
import com.example.fileshare.retrofit.ApiInterface;
import com.example.fileshare.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentFileListActivity extends AppCompatActivity {

    int courseId = 1;
    ArrayList<com.example.fileshare.response.File> list = new ArrayList<>();
    RecyclerView studentFileListRecyclerview;
    StudentFileListAdapter studentFileListAdapter;

    DownloadZipFileTask downloadZipFileTask;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_file_list);

        initialize();
        courseId = getIntent().getIntExtra(Constants.COURSE_ID,1);

        fetchFiles();

        studentFileListAdapter.setOnClickListener(new StudentFileListAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, String fileName) {
                downloadFile(fileName);
            }
        });
        

    }

    private void initialize() {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        studentFileListAdapter = new StudentFileListAdapter(this, list);
        studentFileListRecyclerview = findViewById(R.id.student_file_list_recyclerview);
        studentFileListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        studentFileListRecyclerview.setAdapter(studentFileListAdapter);
    }

    private void fetchFiles() {

        Call<FileResponse> call = apiInterface.getFiles(courseId);
        Log.e("SAN","outside call " + courseId);

        call.enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                Log.e("SAN","inside call");

                list.clear();
                List<com.example.fileshare.response.File> temp = response.body().getFiles();
                Log.e("SAN", list.size() + "");

                for(int i=0; i<temp.size() ; i++){
                    Log.e("SAN",i + "");
                    list.add(temp.get(i));
                }

                studentFileListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {

            }
        });

    }


    private void downloadFile(String fileName) {

        String url = Constants.BASE_STORAGE_URL + fileName;

        Call<ResponseBody> call = apiInterface.downloadFile(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {
                    Log.d("SAN", "Got the body for the file");

                    Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();

                    downloadZipFileTask = new DownloadZipFileTask();
                    downloadZipFileTask.execute(Pair.create(response.body(), "DalalMessage_1553546072.proto"));

                } else {
                    Log.d("SAN", "Connection failed " + response.errorBody());
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("SAN", t.getMessage());
            }
        });
    }

    private class DownloadZipFileTask extends AsyncTask<Pair<ResponseBody, String>, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Pair<ResponseBody, String>... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0].first, urls[0].second);
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.d("API123", progress[0].second + " ");

            if (progress[0].first == 100)
                Toast.makeText(getApplicationContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();


            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
                //progressBar.setProgress(currentProgress);

                //txtProgressPercent.setText("Progress " + currentProgress + "%");

            }

            if (progress[0].first == -1) {
                Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    private void saveToDisk(ResponseBody body, String filename) {
        try {

            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                //Log.d(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                    Log.d("SAN", "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d("SAN", destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d("SAN", "Failed to save the file!");
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SAN", "Failed to save the file!");
            return;
        }
    }
}
