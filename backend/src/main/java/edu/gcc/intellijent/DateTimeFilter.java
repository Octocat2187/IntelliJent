package edu.gcc.intellijent;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DateTimeFilter extends Filter {
    List<ClassTime> schedule = new ArrayList<>();

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {
        ArrayList<Course> filteredResults = new ArrayList<>();

        for (Course course : searchResults) {
            boolean shouldAddCourse = true;
            List<ClassTime> classTimeList = course.getTimes();

            try {
                for (int i=0; i<classTimeList.size(); i++) {
                    String classDay = classTimeList.get(i).getDay();
                    Time classBeginTime = Time.valueOf(classTimeList.get(i).getStart_time());
                    Time classEndTime = Time.valueOf(classTimeList.get(i).getEnd_time());

                    Time filterBeginTime = Time.valueOf(schedule.get(i).getStart_time());
                    Time filterEndTime = Time.valueOf(schedule.get(i).getEnd_time());

                    if(!matchDays(classDay)){
                        shouldAddCourse = false;
                    }

                    if(classBeginTime.before(filterBeginTime)){
                        shouldAddCourse = false;
                    }

                    if(classEndTime.after(filterEndTime)){
                        shouldAddCourse = false;
                    }
                }
            }
            catch (IndexOutOfBoundsException e) {
                //Class times of class were more than the size of the filter's list
                //Do not add to results
                shouldAddCourse = false;
            }

            if(shouldAddCourse) {
                filteredResults.add(course);
            }
        }
        return filteredResults;
    }

    private Boolean matchDays(String day) {
        for(int i = 0; i<schedule.size(); i++){
            String scheduleDay = schedule.get(i).getDay();
            if(scheduleDay.equals(day)){
                return true;
            }
        }
        return false;
    }
}
