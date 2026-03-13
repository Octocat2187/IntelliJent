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
        /**
        for (Course course : searchResults) {
            if((course.getStartTime().compareTo(beginTime)) >= 0 && // Start time of course is after/at start time of filter
                    course.getEndTime().compareTo(endTime) <= 0 && // End time of course is before/at end time of filter
                    course.getDaysOfWeek().equals(days))
            {
                filteredResults.add(course);
            }
        }
    **/
        //TODO: Fix
        return filteredResults;
    }
}
