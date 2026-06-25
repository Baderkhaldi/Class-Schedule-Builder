package com.org.projscheduleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.org.projscheduleapp.models.Course;
import com.org.projscheduleapp.models.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private RecyclerView scheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        FloatingActionButton addCourseFab = findViewById(R.id.addCourseFab);

        // Initialize course list
        courseList = new ArrayList<>();
        loadCourses();

        // Set up RecyclerView with ScheduleAdapter and item interaction handlers
        scheduleAdapter = new ScheduleAdapter(courseList, new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Course course) {
                // Handle item click (e.g., open CourseManagementActivity to edit the course)
                Intent intent = new Intent(Dashboard.this, Course_Management.class);
                intent.putExtra("course", (CharSequence) course); // Pass the selected course to the editing screen
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Course course) {
                // Handle delete action
                DatabaseHelper dbHelper = new DatabaseHelper(Dashboard.this);
                dbHelper.deleteCourse(course.getName());
                loadCourses(); // Refresh the list
                Toast.makeText(Dashboard.this, "Course deleted: " + course.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleRecyclerView.setAdapter(scheduleAdapter);

        // Add course FAB action
        addCourseFab.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, Course_Management.class)));
    }

    private void loadCourses() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        courseList.clear();
        courseList.addAll(dbHelper.getAllCourses());
        scheduleAdapter.notifyDataSetChanged();
    }
}
