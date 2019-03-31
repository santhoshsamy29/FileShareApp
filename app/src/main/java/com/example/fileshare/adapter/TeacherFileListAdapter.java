package com.example.fileshare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fileshare.R;
import com.example.fileshare.response.File;

import java.util.ArrayList;

public class TeacherFileListAdapter extends RecyclerView.Adapter<TeacherFileListAdapter.ViewHolder>{

    Context context;
    ArrayList<File> list = new ArrayList<>();

    public TeacherFileListAdapter(Context context, ArrayList<File> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_file_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.fileNameTextView.setText(list.get(i).getName());
        viewHolder.timeTextView.setText(list.get(i).getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView fileNameTextView, timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileNameTextView = itemView.findViewById(R.id.teacher_file_name_textview_item);

            timeTextView = itemView.findViewById(R.id.teacher_time_textview_item);

        }
    }
}
