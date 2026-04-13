package edu.gcc.intellijent;

import java.util.ArrayList;

public class CourseAddResponse {
    private boolean success;
    private ArrayList<Course> alternativeCourses;
    private boolean courseFull;

    // Constructor for conflict responses (409): provides alternative courses when there's a time conflict
    public CourseAddResponse(boolean success, ArrayList<Course> alternativeCourses) {
        this.success = success;
        this.alternativeCourses = alternativeCourses;
        this.courseFull = false;
    }

    // Constructor for simple responses: used for basic success/failure without additional data
    public CourseAddResponse(boolean success) {
        this.success = success;
        this.alternativeCourses = new ArrayList<>();
        this.courseFull = false;
    }

    // Constructor for successful additions: includes flag indicating if the added course is full
    public CourseAddResponse(boolean success, boolean courseFull, ArrayList<Course> alternativeCourses) {
        this.success = success;
        this.courseFull = courseFull;
        this.alternativeCourses = alternativeCourses;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Course> getAlternativeCourses() {
        return alternativeCourses;
    }

    public void setAlternativeCourses(ArrayList<Course> alternativeCourses) {
        this.alternativeCourses = alternativeCourses;
    }

    public boolean isCourseFull() {
        return courseFull;
    }

    public void setCourseFull(boolean courseFull) {
        this.courseFull = courseFull;
    }
}