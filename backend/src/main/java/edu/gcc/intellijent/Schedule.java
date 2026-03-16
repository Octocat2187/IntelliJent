package edu.gcc.intellijent;
import java.util.ArrayList;
import java.sql.Time;

public class Schedule {
    /**
     * TODO: Change name of this var. It matches the name of the public class Schedule
     * This means to call it we'd have to do something like Schedule.Schedule.add(Course)
     * Which is silly
     */
    ArrayList<Course> Schedule = new ArrayList<Course>();
    public void AddCourse(Course course){
        //TODO: Change this from true to the schedulable check when implemented
        if (isCourseSchedulable(course)) {
            Schedule.add(course);
        }
        else {
            System.out.println("Course cannot be added due to scheduling conflict.");
            //need to add alternative course selection here
        }
    }

    public void RemoveCourse(Course course){
        System.out.println("remove triggered");
        if(Schedule.contains(course)){
            System.out.println("it contains the course");
            Schedule.remove(course);
        }
    }


    /**
     * Checks whether the given course can be added to the schedule without any time overlap.
     * Compares ClassTime objects based on days and times using Time objects.
     * Back-to-back courses (one ends exactly when another starts) are allowed.
     *
     * @param course the course to be added, checked for scheduling conflicts
     * @return true if the course can be scheduled (no overlap), false otherwise
     */
    public boolean isCourseSchedulable(Course course){
        // If course has no class times, it's schedulable
        if (course.getTimes() == null || course.getTimes().isEmpty()) {
            return true;
        }
        // Check against each course already in the schedule
        for (Course scheduledCourse : Schedule) {
            // Compare each ClassTime of the new course with each ClassTime of scheduled courses
            for (ClassTime newTime : course.getTimes()) {
                for (ClassTime scheduledTime : scheduledCourse.getTimes()) {
                    // Check if the classes are on the same day
                    if (newTime.getDay().equals(scheduledTime.getDay())) {
                        // Convert string times to Time objects
                        Time newStart = Time.valueOf(newTime.getStart_time());
                        Time newEnd = Time.valueOf(newTime.getEnd_time());
                        Time scheduledStart = Time.valueOf(scheduledTime.getStart_time());
                        Time scheduledEnd = Time.valueOf(scheduledTime.getEnd_time());

                        // Check if times overlap
                        // Overlap occurs if: new starts before scheduled ends AND new ends after scheduled starts
                        // Back-to-back is allowed, so we use < and > (not <= and >=)
                        if (newStart.compareTo(scheduledEnd) < 0 && newEnd.compareTo(scheduledStart) > 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
