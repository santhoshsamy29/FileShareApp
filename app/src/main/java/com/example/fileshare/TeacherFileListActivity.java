package com.example.fileshare;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fileshare.adapter.TeacherFileListAdapter;
import com.example.fileshare.response.File;
import com.example.fileshare.response.FileResponse;
import com.example.fileshare.response.StandardResponse;
import com.example.fileshare.retrofit.ApiClient;
import com.example.fileshare.retrofit.ApiInterface;
import com.example.fileshare.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherFileListActivity extends AppCompatActivity {

    private static final int REQUEST_PERM = 1;
    int courseId = 1;
    RecyclerView teacherFileListRecyclerview;
    TeacherFileListAdapter teacherFileListAdapter;
    ArrayList<File> list = new ArrayList<>();

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_file_list);

        initialize();
        courseId = getIntent().getIntExtra(Constants.COURSE_ID,1);

        fetchFiles();
    }

    private void fetchFiles() {

        Call<FileResponse> call = apiInterface.getFiles(courseId);
        Log.e("SAN","outside call " + courseId);

        call.enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                Log.e("SAN","inside call");

                list = new ArrayList<>();
                List<File> temp = response.body().getFiles();
                Log.e("SAN", list.size() + "");

                for(int i=0; i<temp.size() ; i++){
                    Log.e("SAN",i + "");
                    list.add(temp.get(i));
                }

                teacherFileListAdapter = new TeacherFileListAdapter(TeacherFileListActivity.this, list);
                teacherFileListRecyclerview.setAdapter(teacherFileListAdapter);
            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {

            }
        });

    }

    private void initialize() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        teacherFileListRecyclerview = findViewById(R.id.teacher_file_list_recyclerview);
        teacherFileListRecyclerview.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_file_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_add_file) {

            String[] permission= new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            if(request(permission,REQUEST_PERM)){
                pickIntent();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public boolean request(String[] permission,int perm_id){
        ArrayList<String> temp_perm = new ArrayList<>();
        for(int i=0 ; i<permission.length ; i++) {
            if (ContextCompat.checkSelfPermission(this, permission[i])
                    != PackageManager.PERMISSION_GRANTED) {
                temp_perm.add(permission[i]);
            }
        }

        String[] perm = new String[temp_perm.size()];
        perm = temp_perm.toArray(perm);

        if(perm.length>0){
            ActivityCompat.requestPermissions(this,perm,perm_id);
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "GRANT ALL PERMISSIONS", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        switch (requestCode) {
            case REQUEST_PERM:
                pickIntent();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PERM:
                    Uri uri = data.getData();
                    Log.e("SAN","uri : " + uri);


                    java.io.File file = new java.io.File(uri.getPath());

                    Log.e("SAN","file : " + file);

                    uploadFile(uri);



                    break;

            }
        }
    }


    public void pickIntent(){
        Intent galIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galIntent.setType("file/*");
        if(galIntent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(galIntent,REQUEST_PERM);
        }
    }


    public static boolean isLocal(String url) {
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        return false;
    }

    /*  public static boolean isLocalStorageDocument(Uri uri) {
          return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
      }
  */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else {
                return DocumentsContract.getDocumentId(uri);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static java.io.File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new java.io.File(path);
            }
        }
        return null;
    }

    private void uploadFile(Uri fileUri) {

        java.io.File file = getFile(this, fileUri);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part multipartBody =MultipartBody.Part.createFormData("file",file.getName(),requestFile);


        // add another part within the multipart request
        String descriptionString = String.valueOf(courseId);
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);
        // finally, execute the request
        Call<StandardResponse> call = apiInterface.upload( multipartBody, description);
        Log.e("SAN","saving calling");

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                Toast.makeText(TeacherFileListActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();

                fetchFiles();
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                Toast.makeText(TeacherFileListActivity.this, "Retry", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
