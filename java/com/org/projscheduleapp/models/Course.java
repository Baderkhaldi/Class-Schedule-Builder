package com.org.projscheduleapp.models;


public class Course {
    private String name, time, location, type;

    public Course(String name, String time, String location, String type) {
        this.name = name;
        this.time = time;
        this.location = location;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }
}
