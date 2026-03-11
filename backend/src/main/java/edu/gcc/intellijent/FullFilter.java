package edu.gcc.intellijent;
import java.util.ArrayList;

public class FullFilter extends Filter {
    public Boolean courseIsFull;

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {

        ArrayList<Course> filteredResults = new ArrayList<>();
        for (Course course : searchResults) {
            if(course.isIs_open() != courseIsFull) {
                filteredResults.add(course);
            }
        }
        return filteredResults;
    }
}
