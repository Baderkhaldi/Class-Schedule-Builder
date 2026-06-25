package com.org.projscheduleapp.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ScheduleBuilder.db";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    private static final String TABLE_COURSES = "courses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_TYPE = "type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_TIME + " TEXT, "
                + COLUMN_LOCATION + " TEXT, "
                + COLUMN_TYPE + " TEXT)";
        db.execSQL(CREATE_COURSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }

    // Add a course
    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, course.getName());
        values.put(COLUMN_TIME, course.getTime());
        values.put(COLUMN_LOCATION, course.getLocation());
        values.put(COLUMN_TYPE, course.getType());

        db.insert(TABLE_COURSES, null, values);
        db.close();
    }

    // Get all courses
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
                );
                courses.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return courses;
    }

    // Delete a course
    public void deleteCourse(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURSES, COLUMN_NAME + "=?", new String[]{name});
        db.close();
    }


    public boolean isConflict(String newCourseTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES,
                new String[]{COLUMN_TIME},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String existingCourseTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
            if (isOverlapping(existingCourseTime, newCourseTime)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    private boolean isOverlapping(String time1, String time2) {
        // Parse times and check for overlap (Assume format "HH:MM AM/PM - HH:MM AM/PM")
        String[] time1Parts = time1.split(" - ");
        String[] time2Parts = time2.split(" - ");

        // Convert times to 24-hour format for easier comparison
        int start1 = parseTimeToMinutes(time1Parts[0]);
        int end1 = parseTimeToMinutes(time1Parts[1]);
        int start2 = parseTimeToMinutes(time2Parts[0]);
        int end2 = parseTimeToMinutes(time2Parts[1]);

        // Check for overlap
        return start1 < end2 && start2 < end1;
    }

    private int parseTimeToMinutes(String time) {
        String[] parts = time.split(" ");
        String[] hhmm = parts[0].split(":");
        int hours = Integer.parseInt(hhmm[0]);
        int minutes = Integer.parseInt(hhmm[1]);
        if (parts[1].equalsIgnoreCase("PM") && hours != 12) {
            hours += 12;
        }
        if (parts[1].equalsIgnoreCase("AM") && hours == 12) {
            hours = 0;
        }
        return hours * 60 + minutes;
    }

}
