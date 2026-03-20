package edu.gcc.intellijent;

import java.util.ArrayList;

public class CourseAddResponse {
    private boolean success;
    private ArrayList<Course> alternativeCourses;

    public CourseAddResponse(boolean success, ArrayList<Course> alternativeCourses) {
        this.success = success;
        this.alternativeCourses = alternativeCourses;
    }

    public CourseAddResponse(boolean success) {
        this.success = success;
        this.alternativeCourses = new ArrayList<>();
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
}

