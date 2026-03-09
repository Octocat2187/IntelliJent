package edu.gcc.intellijent;
import java.sql.Time;
import java.util.ArrayList;

public class DateTimeFilter extends Filter {
    public Time beginTime;
    public Time endTime;
    String days;

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {
        ArrayList<Course> filteredResults = new ArrayList<>();
        for (Course course : searchResults) {
            if(course.getStartTime().equals(beginTime) && course.getEndTime().equals(endTime) && course.getDaysOfWeek().equals(days)){
                filteredResults.add(course);
            }
        }

        return filteredResults;
    }
}
