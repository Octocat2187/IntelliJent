package edu.gcc.intellijent;

import java.util.ArrayList;

public class CourseCodeFilter extends Filter{
    public int courseCode;

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {
        ArrayList<Course> filteredResults = new ArrayList<>();
        for (Course course : searchResults) {
            if(course.getCourseCode() == courseCode){
                filteredResults.add(course);
            }
        }

        return filteredResults;
    }
}
