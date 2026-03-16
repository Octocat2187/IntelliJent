package edu.gcc.intellijent;

import java.util.List;

public class Major {

    private String name;
    private List<String> requiredCourses;

    public String getName() {
        return name;
    }

    public List<String> getRequiredCourses() {
        return requiredCourses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequiredCourses(List<String> requiredCourses) {
        this.requiredCourses = requiredCourses;
    }
}