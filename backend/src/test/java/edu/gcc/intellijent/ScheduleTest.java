package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;



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
        ClassTime time = new ClassTime();
        time.setDay("Monday");
        time.setStart_time("09:00:00");
        time.setEnd_time("10:30:00");
        List<ClassTime> times = new ArrayList<>();
        times.add(time);
        course.setTimes(times);

        Assertions.assertTrue(schedule.isCourseSchedulable(course));
    }

    @Test
    void isCourseSchedulable_NoConflict() {
        // Course scheduled after existing course (with gap)
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("11:00:00");
        newTime.setEnd_time("12:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_BackToBackCourses() {
        // Back-to-back courses should be allowed (one ends exactly when another starts)
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("10:00:00");
        newTime.setEnd_time("11:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_DirectOverlap() {
        // Course overlaps completely with existing course
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("09:00:00");
        newTime.setEnd_time("10:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_PartialOverlapStart() {
        // New course starts before existing course ends
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("08:30:00");
        newTime.setEnd_time("09:30:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_PartialOverlapEnd() {
        // New course ends after existing course starts
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("10:00:00");
        existingTime.setEnd_time("11:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("09:30:00");
        newTime.setEnd_time("10:30:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_CompletelyContained() {
        // New course is completely inside existing course
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("12:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("10:00:00");
        newTime.setEnd_time("11:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_ContainsExisting() {
        // New course completely contains existing course
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("10:00:00");
        existingTime.setEnd_time("11:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("09:00:00");
        newTime.setEnd_time("12:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleCourses_NoConflict() {
        // Multiple courses in schedule, new course doesn't conflict with any
        Schedule schedule = new Schedule();

        Course course1 = new Course();
        ClassTime time1 = new ClassTime();
        time1.setDay("Monday");
        time1.setStart_time("09:00:00");
        time1.setEnd_time("10:00:00");
        List<ClassTime> times1 = new ArrayList<>();
        times1.add(time1);
        course1.setTimes(times1);
        schedule.AddCourse(course1);

        Course course2 = new Course();
        ClassTime time2 = new ClassTime();
        time2.setDay("Monday");
        time2.setStart_time("11:00:00");
        time2.setEnd_time("12:00:00");
        List<ClassTime> times2 = new ArrayList<>();
        times2.add(time2);
        course2.setTimes(times2);
        schedule.AddCourse(course2);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("13:00:00");
        newTime.setEnd_time("14:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleCourses_ConflictWithOne() {
        // Multiple courses in schedule, new course conflicts with one
        Schedule schedule = new Schedule();

        Course course1 = new Course();
        ClassTime time1 = new ClassTime();
        time1.setDay("Monday");
        time1.setStart_time("09:00:00");
        time1.setEnd_time("10:00:00");
        List<ClassTime> times1 = new ArrayList<>();
        times1.add(time1);
        course1.setTimes(times1);
        schedule.AddCourse(course1);

        Course course2 = new Course();
        ClassTime time2 = new ClassTime();
        time2.setDay("Monday");
        time2.setStart_time("11:00:00");
        time2.setEnd_time("12:00:00");
        List<ClassTime> times2 = new ArrayList<>();
        times2.add(time2);
        course2.setTimes(times2);
        schedule.AddCourse(course2);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Monday");
        newTime.setStart_time("11:30:00");
        newTime.setEnd_time("12:30:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_DifferentDays_NoConflict() {
        // Same times but on different days should not conflict
        Schedule schedule = new Schedule();
        Course course1 = new Course();
        ClassTime time1 = new ClassTime();
        time1.setDay("Monday");
        time1.setStart_time("09:00:00");
        time1.setEnd_time("10:00:00");
        List<ClassTime> times1 = new ArrayList<>();
        times1.add(time1);
        course1.setTimes(times1);
        schedule.AddCourse(course1);

        Course newCourse = new Course();
        ClassTime newTime = new ClassTime();
        newTime.setDay("Tuesday");
        newTime.setStart_time("09:00:00");
        newTime.setEnd_time("10:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(newTime);
        newCourse.setTimes(newTimes);

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleDays_NoConflict() {
        // Course offered on multiple days - new course with one day conflict and one day free
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime mondayTime = new ClassTime();
        mondayTime.setDay("Monday");
        mondayTime.setStart_time("09:00:00");
        mondayTime.setEnd_time("10:00:00");
        ClassTime wednesdayTime = new ClassTime();
        wednesdayTime.setDay("Wednesday");
        wednesdayTime.setStart_time("09:00:00");
        wednesdayTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(mondayTime);
        existingTimes.add(wednesdayTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime tuesdayTime = new ClassTime();
        tuesdayTime.setDay("Tuesday");
        tuesdayTime.setStart_time("09:00:00");
        tuesdayTime.setEnd_time("10:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(tuesdayTime);
        newCourse.setTimes(newTimes);

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleDays_ConflictOnOneDay() {
        // Course offered on multiple days - new course conflicts on Monday only
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime mondayTime = new ClassTime();
        mondayTime.setDay("Monday");
        mondayTime.setStart_time("09:00:00");
        mondayTime.setEnd_time("10:00:00");
        ClassTime wednesdayTime = new ClassTime();
        wednesdayTime.setDay("Wednesday");
        wednesdayTime.setStart_time("09:00:00");
        wednesdayTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(mondayTime);
        existingTimes.add(wednesdayTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime mondayNewTime = new ClassTime();
        mondayNewTime.setDay("Monday");
        mondayNewTime.setStart_time("09:30:00");
        mondayNewTime.setEnd_time("10:30:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(mondayNewTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleDays_ConflictOnMultipleDays() {
        // Course offered on multiple days - new course conflicts on multiple days
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime mondayTime = new ClassTime();
        mondayTime.setDay("Monday");
        mondayTime.setStart_time("09:00:00");
        mondayTime.setEnd_time("10:00:00");
        ClassTime wednesdayTime = new ClassTime();
        wednesdayTime.setDay("Wednesday");
        wednesdayTime.setStart_time("09:00:00");
        wednesdayTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(mondayTime);
        existingTimes.add(wednesdayTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime mondayNewTime = new ClassTime();
        mondayNewTime.setDay("Monday");
        mondayNewTime.setStart_time("09:30:00");
        mondayNewTime.setEnd_time("10:30:00");
        ClassTime wednesdayNewTime = new ClassTime();
        wednesdayNewTime.setDay("Wednesday");
        wednesdayNewTime.setStart_time("09:15:00");
        wednesdayNewTime.setEnd_time("10:15:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(mondayNewTime);
        newTimes.add(wednesdayNewTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleDays_PartialOverlapOnMultipleDays() {
        // Course offered Mon/Wed/Fri - new course offered Mon/Wed with conflicts only
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime mondayTime = new ClassTime();
        mondayTime.setDay("Monday");
        mondayTime.setStart_time("09:00:00");
        mondayTime.setEnd_time("10:00:00");
        ClassTime wednesdayTime = new ClassTime();
        wednesdayTime.setDay("Wednesday");
        wednesdayTime.setStart_time("09:00:00");
        wednesdayTime.setEnd_time("10:00:00");
        ClassTime fridayTime = new ClassTime();
        fridayTime.setDay("Friday");
        fridayTime.setStart_time("09:00:00");
        fridayTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(mondayTime);
        existingTimes.add(wednesdayTime);
        existingTimes.add(fridayTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime mondayNewTime = new ClassTime();
        mondayNewTime.setDay("Monday");
        mondayNewTime.setStart_time("09:45:00");
        mondayNewTime.setEnd_time("10:45:00");
        ClassTime wednesdayNewTime = new ClassTime();
        wednesdayNewTime.setDay("Wednesday");
        wednesdayNewTime.setStart_time("08:30:00");
        wednesdayNewTime.setEnd_time("09:30:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(mondayNewTime);
        newTimes.add(wednesdayNewTime);
        newCourse.setTimes(newTimes);

        Assertions.assertFalse(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleDays_NoConflictMultipleDaysScheduled() {
        // Both courses offered on multiple days with no overlaps
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime mondayTime = new ClassTime();
        mondayTime.setDay("Monday");
        mondayTime.setStart_time("09:00:00");
        mondayTime.setEnd_time("10:00:00");
        ClassTime wednesdayTime = new ClassTime();
        wednesdayTime.setDay("Wednesday");
        wednesdayTime.setStart_time("09:00:00");
        wednesdayTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(mondayTime);
        existingTimes.add(wednesdayTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime tuesdayTime = new ClassTime();
        tuesdayTime.setDay("Tuesday");
        tuesdayTime.setStart_time("09:00:00");
        tuesdayTime.setEnd_time("10:00:00");
        ClassTime fridayTime = new ClassTime();
        fridayTime.setDay("Friday");
        fridayTime.setStart_time("11:00:00");
        fridayTime.setEnd_time("12:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(tuesdayTime);
        newTimes.add(fridayTime);
        newCourse.setTimes(newTimes);

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void isCourseSchedulable_MultipleDays_BackToBackOnDifferentDays() {
        // Back-to-back courses on different days should not conflict
        Schedule schedule = new Schedule();
        Course existingCourse = new Course();
        ClassTime mondayTime = new ClassTime();
        mondayTime.setDay("Monday");
        mondayTime.setStart_time("09:00:00");
        mondayTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(mondayTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        ClassTime tuesdayTime = new ClassTime();
        tuesdayTime.setDay("Tuesday");
        tuesdayTime.setStart_time("10:00:00");
        tuesdayTime.setEnd_time("11:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(tuesdayTime);
        newCourse.setTimes(newTimes);

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }
}