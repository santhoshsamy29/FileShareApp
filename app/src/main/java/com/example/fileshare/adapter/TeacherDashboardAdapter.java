package com.example.fileshare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fileshare.R;
import com.example.fileshare.response.TeacherCourse;

import java.util.ArrayList;

public class TeacherDashboardAdapter extends RecyclerView.Adapter<TeacherDashboardAdapter.ViewHolder> {

    private ArrayList<TeacherCourse> list;
    private Context context;
    OnClickListener onClickListener;

    public void setOnClickListener(TeacherDashboardAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public TeacherDashboardAdapter(Context context, ArrayList<TeacherCourse> list) {

        this.list = list;
        Log.e("SAN", "" + list.size());
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_courses_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.e("SAN", "adapter called");

        viewHolder.courseName.setText(list.get(i).getCourseName());
        viewHolder.className.setText(String.format("%s - %s, %s Year", list.get(i).getDepartment(), list.get(i).getSection(), list.get(i).getCourseYear()));

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.itemClicked(v, list.get(i).getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView  className, courseName;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            layout = itemView.findViewById(R.id.student_courses_layout_item);
            className = itemView.findViewById(R.id.teacher_name_textview_item);
            courseName = itemView.findViewById(R.id.course_name_textview_item);
        }
    }


    public interface OnClickListener{
        public void itemClicked(View view, int courseId);
    }
}
