package edu.gcc.intellijent;
import java.util.ArrayList;

public class ProfFilter extends Filter {
    public String prof;

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {
        ArrayList<Course> filteredResults = new ArrayList<>();
        for (Course course : searchResults) {
            if(course.getProfessor().equals(prof)){
                filteredResults.add(course);
            }
        }

        return filteredResults;
    }
}
