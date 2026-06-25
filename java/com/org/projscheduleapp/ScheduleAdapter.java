package com.org.projscheduleapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.projscheduleapp.models.Course;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private final List<Course> courseList;
    private final OnItemClickListener onItemClickListener;

    public ScheduleAdapter(List<Course> courseList, OnItemClickListener onItemClickListener) {
        this.courseList = courseList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Course course);
        void onDeleteClick(Course course);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseNameTextView.setText(course.getName());
        holder.courseTimeTextView.setText(course.getTime());
        holder.courseLocationTextView.setText(course.getLocation());
        holder.courseTypeTextView.setText(course.getType());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(course));
        holder.deleteButton.setOnClickListener(v -> onItemClickListener.onDeleteClick(course));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameTextView, courseTimeTextView, courseLocationTextView, courseTypeTextView;
        View deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
            courseTimeTextView = itemView.findViewById(R.id.courseTimeTextView);
            courseLocationTextView = itemView.findViewById(R.id.courseLocationTextView);
            courseTypeTextView = itemView.findViewById(R.id.courseTypeTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
