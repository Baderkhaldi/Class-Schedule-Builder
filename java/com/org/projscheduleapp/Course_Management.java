package com.org.projscheduleapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.org.projscheduleapp.models.Course;
import com.org.projscheduleapp.models.DatabaseHelper;

public class Course_Management extends AppCompatActivity {

    private EditText courseNameEditText, courseTimeEditText, courseLocationEditText;
    private Spinner courseTypeSpinner;
    private Button saveCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_management);

        courseNameEditText = findViewById(R.id.courseNameEditText);
        courseTimeEditText = findViewById(R.id.courseTimeEditText);
        courseLocationEditText = findViewById(R.id.courseLocationEditText);
        courseTypeSpinner = findViewById(R.id.courseTypeSpinner);
        saveCourseButton = findViewById(R.id.saveCourseButton);

        saveCourseButton.setOnClickListener(v -> saveCourse());
    }

    private void saveCourse() {
        String name = courseNameEditText.getText().toString().trim();
        String time = courseTimeEditText.getText().toString().trim();
        String location = courseLocationEditText.getText().toString().trim();
        String type = courseTypeSpinner.getSelectedItem().toString();

        if (name.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.addCourse(new Course(name, time, location, type));
        Toast.makeText(this, "Course saved: " + name, Toast.LENGTH_SHORT).show();
        finish();
    }

}
