package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Time;



class ScheduleTest {

    @Test
    void addCourse() {
        Course course = new Course();
        Schedule schedule = new Schedule();

        Assertions.assertNotEquals(1, schedule.Schedule.size());

        schedule.AddCourse(course);
        Assertions.assertEquals(1, schedule.Schedule.size());
    }

    @Test
    void removeCourse() {
        Course course = new Course();
        Schedule schedule = new Schedule();
        schedule.AddCourse(course);

        Assertions.assertEquals(1, schedule.Schedule.size());

        schedule.RemoveCourse(course);
        Assertions.assertEquals(0, schedule.Schedule.size());
    }

    // Test cases for isCourseSchedulable method

    @Test
    void isCourseSchedulable_EmptySchedule() {
        // A course should be schedulable when schedule is empty
        Schedule schedule = new Schedule();
        Course course = new Course();
        course.setStartTime(Time.valueOf("09:00:00"));
        course.setEndTime(Time.valueOf("10:30:00"));

        Assertions.assertTrue(schedule.isCourseSchedulable(course));
    }

    @Test
    void isCourseSchedulable_NoConflict() {
        // Course scheduled after existing course (with gap)
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        existingCourse.setStartTime(Time.valueOf("09:00:00"));
        existingCourse.setEndTime(Time.valueOf("10:00:00"));
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("11:00:00"));
        newCourse.setEndTime(Time.valueOf("12:00:00"));

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_BackToBackCourses() {
        // Back-to-back courses should be allowed (one ends exactly when another starts)
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        existingCourse.setStartTime(Time.valueOf("09:00:00"));
        existingCourse.setEndTime(Time.valueOf("10:00:00"));
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("10:00:00"));
        newCourse.setEndTime(Time.valueOf("11:00:00"));

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_DirectOverlap() {
        // Course overlaps completely with existing course
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        existingCourse.setStartTime(Time.valueOf("09:00:00"));
        existingCourse.setEndTime(Time.valueOf("10:00:00"));
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("09:00:00"));
        newCourse.setEndTime(Time.valueOf("10:00:00"));

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_PartialOverlapStart() {
        // New course starts before existing course ends
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        existingCourse.setStartTime(Time.valueOf("09:00:00"));
        existingCourse.setEndTime(Time.valueOf("10:00:00"));
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("08:30:00"));
        newCourse.setEndTime(Time.valueOf("09:30:00"));

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_PartialOverlapEnd() {
        // New course ends after existing course starts
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        existingCourse.setStartTime(Time.valueOf("10:00:00"));
        existingCourse.setEndTime(Time.valueOf("11:00:00"));
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("09:30:00"));
        newCourse.setEndTime(Time.valueOf("10:30:00"));

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_CompletelyContained() {
        // New course is completely inside existing course
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        existingCourse.setStartTime(Time.valueOf("09:00:00"));
        existingCourse.setEndTime(Time.valueOf("12:00:00"));
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("10:00:00"));
        newCourse.setEndTime(Time.valueOf("11:00:00"));

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_ContainsExisting() {
        // New course completely contains existing course
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        existingCourse.setStartTime(Time.valueOf("10:00:00"));
        existingCourse.setEndTime(Time.valueOf("11:00:00"));
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("09:00:00"));
        newCourse.setEndTime(Time.valueOf("12:00:00"));

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleCourses_NoConflict() {
        // Multiple courses in schedule, new course doesn't conflict with any
        Schedule schedule = new Schedule();

        Course course1 = new Course();
        course1.setStartTime(Time.valueOf("09:00:00"));
        course1.setEndTime(Time.valueOf("10:00:00"));
        schedule.AddCourse(course1);

        Course course2 = new Course();
        course2.setStartTime(Time.valueOf("11:00:00"));
        course2.setEndTime(Time.valueOf("12:00:00"));
        schedule.AddCourse(course2);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("13:00:00"));
        newCourse.setEndTime(Time.valueOf("14:00:00"));

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleCourses_ConflictWithOne() {
        // Multiple courses in schedule, new course conflicts with one
        Schedule schedule = new Schedule();

        Course course1 = new Course();
        course1.setStartTime(Time.valueOf("09:00:00"));
        course1.setEndTime(Time.valueOf("10:00:00"));
        schedule.AddCourse(course1);

        Course course2 = new Course();
        course2.setStartTime(Time.valueOf("11:00:00"));
        course2.setEndTime(Time.valueOf("12:00:00"));
        schedule.AddCourse(course2);

        Course newCourse = new Course();
        newCourse.setStartTime(Time.valueOf("11:30:00"));
        newCourse.setEndTime(Time.valueOf("12:30:00"));

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }
}