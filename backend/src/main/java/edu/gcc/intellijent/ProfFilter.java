package edu.gcc.intellijent;
import java.util.ArrayList;

public class ProfFilter extends Filter {
    public ArrayList<String> prof;

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {
        ArrayList<Course> filteredResults = new ArrayList<>();
        for (Course course : searchResults) {
            if(course.getFaculty().equals(prof)){
                filteredResults.add(course);
            }
        }

        return filteredResults;
    }
}
