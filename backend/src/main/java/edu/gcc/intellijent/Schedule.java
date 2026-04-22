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
    boolean CourseAdded = false;
    boolean courseFull = false;
    public void AddCourse(Course course){
        if (isCourseSchedulable(course)) {
            courseFull = !course.isAvailable();
            Schedule.add(course);
            CourseAdded = true;
        }
        else {
            CourseAdded = false;
        }
    }

    public void RemoveCourse(Course course){
        System.out.println("remove triggered");
        if(Schedule.contains(course)){
            System.out.println("it contains the course");
            Schedule.remove(course);
        }
    }

    public void clearSchedule(){
        Schedule.clear();
    }


    /**
     * Checks whether the given course can be added to the schedule without any time overlap
     * and if the course has available seats.
     * Compares ClassTime objects based on days and times using Time objects.
     * Back-to-back courses (one ends exactly when another starts) are allowed.
     *
     * @param course the course to be added, checked for scheduling conflicts and availability
     * @return true if the course can be scheduled (no overlap and has available seats), false otherwise
     */
    public boolean isCourseSchedulable(Course course){

        // If course has no class times, it's schedulable
        if (course.getTimes() == null || course.getTimes().isEmpty()) {
            return true;
        }
        // Check against each course already in the schedule
        for (Course scheduledCourse : Schedule) {
            // Don't add if it's the same course (same subject and number, regardless of section)
            if (course.getSubject() != null && course.getSubject().equals(scheduledCourse.getSubject()) &&
                course.getNumber() == scheduledCourse.getNumber()) {
                return false; // dont add courses that are already in the schedule.
            }
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

    /**
     * Finds alternative sections/times of the same course that can be scheduled without conflicts.
     * Uses the Search class to find all available sections of the course, then filters them
     * to find alternatives that don't have scheduling conflicts.
     *
     * @param course the course for which to find alternatives
     * @param courseCatalog the catalog containing all available courses
     * @return an ArrayList of alternative course sections that are schedulable, or empty if course already scheduled
     */
    public ArrayList<Course> findAlternativeCourses(Course course, CourseCatalog courseCatalog) {
        ArrayList<Course> alternativeCourses = new ArrayList<>();
        // Create a Search object with the course catalog and search by course code
        Search Coursename = new Search(courseCatalog, course.getName()); // Assuming course name includes code, adjust if needed
        ArrayList<Course> searchResults = Coursename.GetResultList(); // Get search results for the course name

        // Filter results to find only schedulable alternatives
        if (searchResults != null) { //skip if no results found
            for (Course alternative : searchResults) {
                // Skip if it's the exact same section being re-added
                if (alternative.getSection() != null && alternative.getSection().equals(course.getSection())) {
                    continue;
                }
                if (Schedule.contains(alternative)) {
                    continue;
                }
                // Check if the alternative course can be scheduled (time conflicts, etc)
                if (isCourseSchedulable(alternative)) {
                    alternativeCourses.add(alternative);
                }
            }
        }
        return alternativeCourses;
    }

    public boolean wonRoulette(int guess){
        int winningNumber = (int)(Math.random() * 50) + 1;

        return guess == winningNumber;
    }

    public boolean isCourseAdded(){
        return CourseAdded;
    }

    public boolean isCourseFull(){
        return courseFull;
    }

}
