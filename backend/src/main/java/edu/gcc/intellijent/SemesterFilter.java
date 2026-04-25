package edu.gcc.intellijent;

import java.util.ArrayList;

public class SemesterFilter extends Filter{
    public String semester;

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {
        ArrayList<Course> filteredResults = new ArrayList<>();
        for(Course course : searchResults){
            if(course.getSemester().equals(semester)){
                filteredResults.add(course);
            }
        }

        return filteredResults;
    }
}
