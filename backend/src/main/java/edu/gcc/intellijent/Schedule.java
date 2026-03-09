package edu.gcc.intellijent;
import java.util.ArrayList;
public class Schedule {
    ArrayList<Course> Schedule = new ArrayList<Course>();
    public void AddCourse(Course course){
        if (isCourseSchedulable(course)) {
            Schedule.add(course);
        }
        else {
            System.out.println("Course cannot be added due to scheduling conflict.");
            //need to add alternative course selection here
        }
    }

    public void RemoveCourse(Course course){

    }

    /**
     * Checks whether the given course can be added to the schedule without any time overlap.
     * Back-to-back courses (one ends exactly when another starts) are allowed.
     *
     * @param course the to be Added course checked for scheduling conflicts
     * @return true if the course can be scheduled (no overlap), false otherwise
     */
//    public boolean isCourseSchedulable(Course course){
//        for (int i = 0; i < Schedule.size(); i++) {
//           Course schCor = Schedule.get(i);
//            if (course.getStartTime() < schCor.getEndTime() &&
//                    course.getEndTime() > schCor.getStartTime()) {
//                return false;
//            }
//        }
//        return true;
//    }

}
