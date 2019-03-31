package com.example.fileshare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fileshare.R;
import com.example.fileshare.response.File;

import java.util.ArrayList;

public class StudentFileListAdapter extends RecyclerView.Adapter<StudentFileListAdapter.ViewHolder> {

    Context context;
    ArrayList<File> list;

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }



    public StudentFileListAdapter(Context context, ArrayList<File> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_file_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.time.setText(list.get(i).getCreatedAt());
        viewHolder.fileName.setText(list.get(i).getName());

        viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.itemClicked(v, list.get(i).getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView fileName, time;
        ImageButton downloadButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.student_file_name_textview_item);
            time = itemView.findViewById(R.id.student_time_textview_item);
            downloadButton = itemView.findViewById(R.id.download_file_button);
        }
    }

    public interface OnClickListener{
        void itemClicked(View view, String fileName);
    }
}
