package edu.gcc.intellijent;
import java.util.ArrayList;

public class CreditsFilter extends Filter{
    public int credits;

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {
        ArrayList<Course> filteredResults = new ArrayList<>();
        for(Course course : searchResults){
            if(course.getNumCredits() == credits){
                filteredResults.add(course);
            }
        }

        return filteredResults;
    }
}
