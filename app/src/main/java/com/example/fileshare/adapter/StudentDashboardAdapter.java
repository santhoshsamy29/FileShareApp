package com.example.fileshare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fileshare.R;
import com.example.fileshare.response.Enrollment;
import com.example.fileshare.response.EnrollmentResponse;

import java.util.ArrayList;

import retrofit2.Callback;

public class StudentDashboardAdapter extends RecyclerView.Adapter<StudentDashboardAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Enrollment> map = new ArrayList<>();

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public StudentDashboardAdapter(Context context, ArrayList<Enrollment> map) {

        this.context = context;
        this.map = map;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_courses_list_item, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.courseName.setText(map.get(i).getCourseName());
        viewHolder.teacherName.setText(map.get(i).getTeacherName());

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.itemClicked(v, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseName, teacherName;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.student_courses_layout_item);
            teacherName = itemView.findViewById(R.id.teacher_name_textview_item);
            courseName = itemView.findViewById(R.id.course_name_textview_item);
        }
    }

    public interface OnClickListener {
        void itemClicked(View view, int position);
    }
}
