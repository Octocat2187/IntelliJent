package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;



class ScheduleTest {

    @Test
    void addCourse() {
        Course course = new Course();
        course.setOpen_seats(1);
        course.setTotal_seats(30);
        Schedule schedule = new Schedule();

        Assertions.assertNotEquals(1, schedule.Schedule.size());

        schedule.AddCourse(course);
        Assertions.assertEquals(1, schedule.Schedule.size());
    }

    @Test
    void removeCourse() {
        Course course = new Course();
        course.setOpen_seats(1);
        course.setTotal_seats(30);
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
        course.setOpen_seats(1);
        course.setTotal_seats(30);
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
    void isCourseSchedulable_CourseIsFull() {
        // A course that is full (no open seats) should not be schedulable
        Schedule schedule = new Schedule();
        Course course = new Course();
        course.setOpen_seats(0);
        course.setTotal_seats(30);
        ClassTime time = new ClassTime();
        time.setDay("Monday");
        time.setStart_time("09:00:00");
        time.setEnd_time("10:30:00");
        List<ClassTime> times = new ArrayList<>();
        times.add(time);
        course.setTimes(times);

        Assertions.assertFalse(schedule.isCourseSchedulable(course));
    }

    @Test
    void isCourseSchedulable_CourseHasNoAvailableSeats() {
        // A course with no available seats (full) should not be schedulable
        Schedule schedule = new Schedule();
        Course course = new Course();
        course.setOpen_seats(0);
        course.setTotal_seats(30);
        ClassTime time = new ClassTime();
        time.setDay("Monday");
        time.setStart_time("09:00:00");
        time.setEnd_time("10:30:00");
        List<ClassTime> times = new ArrayList<>();
        times.add(time);
        course.setTimes(times);

        Assertions.assertFalse(schedule.isCourseSchedulable(course));
    }

    @Test
    void isCourseSchedulable_CourseHasAvailableSeats() {
        // A course with available seats should be schedulable
        Schedule schedule = new Schedule();
        Course course = new Course();
        course.setOpen_seats(1);
        course.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("10:00:00");
        existingTime.setEnd_time("11:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("12:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("10:00:00");
        existingTime.setEnd_time("11:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        course1.setOpen_seats(1);
        course1.setTotal_seats(30);
        ClassTime time1 = new ClassTime();
        time1.setDay("Monday");
        time1.setStart_time("09:00:00");
        time1.setEnd_time("10:00:00");
        List<ClassTime> times1 = new ArrayList<>();
        times1.add(time1);
        course1.setTimes(times1);
        schedule.AddCourse(course1);

        Course course2 = new Course();
        course2.setOpen_seats(1);
        course2.setTotal_seats(30);
        ClassTime time2 = new ClassTime();
        time2.setDay("Monday");
        time2.setStart_time("11:00:00");
        time2.setEnd_time("12:00:00");
        List<ClassTime> times2 = new ArrayList<>();
        times2.add(time2);
        course2.setTimes(times2);
        schedule.AddCourse(course2);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        course1.setOpen_seats(1);
        course1.setTotal_seats(30);
        ClassTime time1 = new ClassTime();
        time1.setDay("Monday");
        time1.setStart_time("09:00:00");
        time1.setEnd_time("10:00:00");
        List<ClassTime> times1 = new ArrayList<>();
        times1.add(time1);
        course1.setTimes(times1);
        schedule.AddCourse(course1);

        Course course2 = new Course();
        course2.setOpen_seats(1);
        course2.setTotal_seats(30);
        ClassTime time2 = new ClassTime();
        time2.setDay("Monday");
        time2.setStart_time("11:00:00");
        time2.setEnd_time("12:00:00");
        List<ClassTime> times2 = new ArrayList<>();
        times2.add(time2);
        course2.setTimes(times2);
        schedule.AddCourse(course2);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        course1.setOpen_seats(1);
        course1.setTotal_seats(30);
        ClassTime time1 = new ClassTime();
        time1.setDay("Monday");
        time1.setStart_time("09:00:00");
        time1.setEnd_time("10:00:00");
        List<ClassTime> times1 = new ArrayList<>();
        times1.add(time1);
        course1.setTimes(times1);
        schedule.AddCourse(course1);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
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
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
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
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
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
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
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
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
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
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
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
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        ClassTime mondayTime = new ClassTime();
        mondayTime.setDay("Monday");
        mondayTime.setStart_time("09:00:00");
        mondayTime.setEnd_time("10:00:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(mondayTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        Course newCourse = new Course();
        newCourse.setOpen_seats(1);
        newCourse.setTotal_seats(30);
        ClassTime tuesdayTime = new ClassTime();
        tuesdayTime.setDay("Tuesday");
        tuesdayTime.setStart_time("10:00:00");
        tuesdayTime.setEnd_time("11:00:00");
        List<ClassTime> newTimes = new ArrayList<>();
        newTimes.add(tuesdayTime);
        newCourse.setTimes(newTimes);

        Assertions.assertTrue(schedule.isCourseSchedulable(newCourse));
    }

    @Test
    void findAlternativeCourses_NoAlternativeSections() {
        // Only one section of the course exists, so no alternatives available
        Schedule schedule = new Schedule();
        Course targetCourse = new Course();
        targetCourse.setSubject("CS");
        targetCourse.setNumber(101);
        targetCourse.setName("Intro to CS");
        targetCourse.setSection("01");
        targetCourse.setOpen_seats(1);
        targetCourse.setTotal_seats(30);
        targetCourse.setFaculty(new ArrayList<>()); // Initialize faculty list
        ClassTime time = new ClassTime();
        time.setDay("Monday");
        time.setStart_time("09:00:00");
        time.setEnd_time("10:30:00");
        List<ClassTime> times = new ArrayList<>();
        times.add(time);
        targetCourse.setTimes(times);

        CourseCatalog catalog = new CourseCatalog();
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(targetCourse);
        catalog.setClasses(courses);

        ArrayList<Course> alternatives = schedule.findAlternativeCourses(targetCourse, catalog);

        Assertions.assertEquals(0, alternatives.size());
    }

    @Test
    void findAlternativeCourses_OneSchedulableAlternative() {
        // Multiple sections exist, but only one is schedulable
        Schedule schedule = new Schedule();

        // Add existing course to schedule
        Course existingCourse = new Course();
        existingCourse.setSubject("CS");
        existingCourse.setNumber(101);
        existingCourse.setName("Intro to CS");
        existingCourse.setSection("01");
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        existingCourse.setFaculty(new ArrayList<>());
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:30:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        // Target course (section 02 - conflicts with existing)
        Course targetCourse = new Course();
        targetCourse.setSubject("CS");
        targetCourse.setNumber(101);
        targetCourse.setName("Intro to CS");
        targetCourse.setSection("02");
        targetCourse.setOpen_seats(1);
        targetCourse.setTotal_seats(30);
        targetCourse.setFaculty(new ArrayList<>());
        ClassTime targetTime = new ClassTime();
        targetTime.setDay("Monday");
        targetTime.setStart_time("09:00:00");
        targetTime.setEnd_time("10:30:00");
        List<ClassTime> targetTimes = new ArrayList<>();
        targetTimes.add(targetTime);
        targetCourse.setTimes(targetTimes);

        // Alternative section 03 (no conflict)
        Course alternativeCourse = new Course();
        alternativeCourse.setSubject("CS");
        alternativeCourse.setNumber(101);
        alternativeCourse.setName("Intro to CS");
        alternativeCourse.setSection("03");
        alternativeCourse.setOpen_seats(1);
        alternativeCourse.setTotal_seats(30);
        alternativeCourse.setFaculty(new ArrayList<>());
        ClassTime altTime = new ClassTime();
        altTime.setDay("Tuesday");
        altTime.setStart_time("11:00:00");
        altTime.setEnd_time("12:30:00");
        List<ClassTime> altTimes = new ArrayList<>();
        altTimes.add(altTime);
        alternativeCourse.setTimes(altTimes);

        CourseCatalog catalog = new CourseCatalog();
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(targetCourse);
        courses.add(alternativeCourse);
        catalog.setClasses(courses);

        ArrayList<Course> alternatives = schedule.findAlternativeCourses(targetCourse, catalog);

        Assertions.assertEquals(1, alternatives.size());
        Assertions.assertEquals("03", alternatives.get(0).getSection());
    }

    @Test
    void findAlternativeCourses_MultipleSchedulableAlternatives() {
        // Multiple schedulable alternatives available
        Schedule schedule = new Schedule();

        // Add existing course to schedule
        Course existingCourse = new Course();
        existingCourse.setSubject("CS");
        existingCourse.setNumber(101);
        existingCourse.setName("Intro to CS");
        existingCourse.setSection("01");
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        existingCourse.setFaculty(new ArrayList<>());
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:30:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        // Target course (section 02 - conflicts)
        Course targetCourse = new Course();
        targetCourse.setSubject("CS");
        targetCourse.setNumber(101);
        targetCourse.setName("Intro to CS");
        targetCourse.setSection("02");
        targetCourse.setOpen_seats(1);
        targetCourse.setTotal_seats(30);
        targetCourse.setFaculty(new ArrayList<>());
        ClassTime targetTime = new ClassTime();
        targetTime.setDay("Monday");
        targetTime.setStart_time("09:00:00");
        targetTime.setEnd_time("10:30:00");
        List<ClassTime> targetTimes = new ArrayList<>();
        targetTimes.add(targetTime);
        targetCourse.setTimes(targetTimes);

        // Alternative section 03
        Course alternative1 = new Course();
        alternative1.setSubject("CS");
        alternative1.setNumber(101);
        alternative1.setName("Intro to CS");
        alternative1.setSection("03");
        alternative1.setOpen_seats(1);
        alternative1.setTotal_seats(30);
        alternative1.setFaculty(new ArrayList<>());
        ClassTime alt1Time = new ClassTime();
        alt1Time.setDay("Tuesday");
        alt1Time.setStart_time("11:00:00");
        alt1Time.setEnd_time("12:30:00");
        List<ClassTime> alt1Times = new ArrayList<>();
        alt1Times.add(alt1Time);
        alternative1.setTimes(alt1Times);

        // Alternative section 04
        Course alternative2 = new Course();
        alternative2.setSubject("CS");
        alternative2.setNumber(101);
        alternative2.setName("Intro to CS");
        alternative2.setSection("04");
        alternative2.setOpen_seats(1);
        alternative2.setTotal_seats(30);
        alternative2.setFaculty(new ArrayList<>());
        ClassTime alt2Time = new ClassTime();
        alt2Time.setDay("Wednesday");
        alt2Time.setStart_time("14:00:00");
        alt2Time.setEnd_time("15:30:00");
        List<ClassTime> alt2Times = new ArrayList<>();
        alt2Times.add(alt2Time);
        alternative2.setTimes(alt2Times);

        CourseCatalog catalog = new CourseCatalog();
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(targetCourse);
        courses.add(alternative1);
        courses.add(alternative2);
        catalog.setClasses(courses);

        ArrayList<Course> alternatives = schedule.findAlternativeCourses(targetCourse, catalog);

        Assertions.assertEquals(2, alternatives.size());
    }

    @Test
    void findAlternativeCourses_NoSchedulableAlternatives() {
        // Multiple alternatives exist, but none are schedulable
        Schedule schedule = new Schedule();

        // Add existing course to schedule
        Course existingCourse = new Course();
        existingCourse.setSubject("CS");
        existingCourse.setNumber(101);
        existingCourse.setName("Intro to CS");
        existingCourse.setSection("01");
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        existingCourse.setFaculty(new ArrayList<>());
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:30:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        // Target course (section 02 - conflicts)
        Course targetCourse = new Course();
        targetCourse.setSubject("CS");
        targetCourse.setNumber(101);
        targetCourse.setName("Intro to CS");
        targetCourse.setSection("02");
        targetCourse.setOpen_seats(1);
        targetCourse.setTotal_seats(30);
        targetCourse.setFaculty(new ArrayList<>());
        ClassTime targetTime = new ClassTime();
        targetTime.setDay("Monday");
        targetTime.setStart_time("09:00:00");
        targetTime.setEnd_time("10:30:00");
        List<ClassTime> targetTimes = new ArrayList<>();
        targetTimes.add(targetTime);
        targetCourse.setTimes(targetTimes);

        // Alternative section 03 - also conflicts
        Course alternative1 = new Course();
        alternative1.setSubject("CS");
        alternative1.setNumber(101);
        alternative1.setName("Intro to CS");
        alternative1.setSection("03");
        alternative1.setOpen_seats(1);
        alternative1.setTotal_seats(30);
        alternative1.setFaculty(new ArrayList<>());
        ClassTime alt1Time = new ClassTime();
        alt1Time.setDay("Monday");
        alt1Time.setStart_time("09:30:00");
        alt1Time.setEnd_time("11:00:00");
        List<ClassTime> alt1Times = new ArrayList<>();
        alt1Times.add(alt1Time);
        alternative1.setTimes(alt1Times);

        // Alternative section 04 - also conflicts
        Course alternative2 = new Course();
        alternative2.setSubject("CS");
        alternative2.setNumber(101);
        alternative2.setName("Intro to CS");
        alternative2.setSection("04");
        alternative2.setOpen_seats(1);
        alternative2.setTotal_seats(30);
        alternative2.setFaculty(new ArrayList<>());
        ClassTime alt2Time = new ClassTime();
        alt2Time.setDay("Monday");
        alt2Time.setStart_time("08:30:00");
        alt2Time.setEnd_time("09:45:00");
        List<ClassTime> alt2Times = new ArrayList<>();
        alt2Times.add(alt2Time);
        alternative2.setTimes(alt2Times);

        CourseCatalog catalog = new CourseCatalog();
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(targetCourse);
        courses.add(alternative1);
        courses.add(alternative2);
        catalog.setClasses(courses);

        ArrayList<Course> alternatives = schedule.findAlternativeCourses(targetCourse, catalog);

        Assertions.assertEquals(0, alternatives.size());
    }

    @Test
    void findAlternativeCourses_AlternativeWithMultipleDays() {
        // Find alternative section that meets on multiple days
        Schedule schedule = new Schedule();

        // Add existing course to schedule
        Course existingCourse = new Course();
        existingCourse.setSubject("MATH");
        existingCourse.setNumber(201);
        existingCourse.setName("Calculus I");
        existingCourse.setSection("01");
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        existingCourse.setFaculty(new ArrayList<>());
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:30:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        // Target course (section 01 - conflicts on Monday)
        Course targetCourse = new Course();
        targetCourse.setSubject("MATH");
        targetCourse.setNumber(201);
        targetCourse.setName("Calculus I");
        targetCourse.setSection("01");
        targetCourse.setOpen_seats(1);
        targetCourse.setTotal_seats(30);
        targetCourse.setFaculty(new ArrayList<>());
        ClassTime targetTime = new ClassTime();
        targetTime.setDay("Monday");
        targetTime.setStart_time("09:00:00");
        targetTime.setEnd_time("10:30:00");
        List<ClassTime> targetTimes = new ArrayList<>();
        targetTimes.add(targetTime);
        targetCourse.setTimes(targetTimes);

        // Alternative section 02 - meets Tue/Thu
        Course alternative = new Course();
        alternative.setSubject("MATH");
        alternative.setNumber(201);
        alternative.setName("Calculus I");
        alternative.setSection("02");
        alternative.setOpen_seats(1);
        alternative.setTotal_seats(30);
        alternative.setFaculty(new ArrayList<>());
        ClassTime tuesdayTime = new ClassTime();
        tuesdayTime.setDay("Tuesday");
        tuesdayTime.setStart_time("11:00:00");
        tuesdayTime.setEnd_time("12:30:00");
        ClassTime thursdayTime = new ClassTime();
        thursdayTime.setDay("Thursday");
        thursdayTime.setStart_time("11:00:00");
        thursdayTime.setEnd_time("12:30:00");
        List<ClassTime> altTimes = new ArrayList<>();
        altTimes.add(tuesdayTime);
        altTimes.add(thursdayTime);
        alternative.setTimes(altTimes);

        CourseCatalog catalog = new CourseCatalog();
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(targetCourse);
        courses.add(alternative);
        catalog.setClasses(courses);

        ArrayList<Course> alternatives = schedule.findAlternativeCourses(targetCourse, catalog);

        Assertions.assertEquals(1, alternatives.size());
        Assertions.assertEquals("02", alternatives.get(0).getSection());
    }

    @Test
    void findAlternativeCourses_MixedResults() {
        // Catalog has some schedulable and some non-schedulable alternatives
        Schedule schedule = new Schedule();

        // Add existing course to schedule
        Course existingCourse = new Course();
        existingCourse.setSubject("PHYS");
        existingCourse.setNumber(150);
        existingCourse.setName("Physics I");
        existingCourse.setSection("01");
        existingCourse.setOpen_seats(1);
        existingCourse.setTotal_seats(30);
        existingCourse.setFaculty(new ArrayList<>());
        ClassTime existingTime = new ClassTime();
        existingTime.setDay("Monday");
        existingTime.setStart_time("09:00:00");
        existingTime.setEnd_time("10:30:00");
        List<ClassTime> existingTimes = new ArrayList<>();
        existingTimes.add(existingTime);
        existingCourse.setTimes(existingTimes);
        schedule.AddCourse(existingCourse);

        // Target course
        Course targetCourse = new Course();
        targetCourse.setSubject("PHYS");
        targetCourse.setNumber(150);
        targetCourse.setName("Physics I");
        targetCourse.setSection("02");
        targetCourse.setOpen_seats(1);
        targetCourse.setTotal_seats(30);
        targetCourse.setFaculty(new ArrayList<>());
        ClassTime targetTime = new ClassTime();
        targetTime.setDay("Monday");
        targetTime.setStart_time("09:00:00");
        targetTime.setEnd_time("10:30:00");
        List<ClassTime> targetTimes = new ArrayList<>();
        targetTimes.add(targetTime);
        targetCourse.setTimes(targetTimes);

        // Alternative 03 - conflicts
        Course alt1 = new Course();
        alt1.setSubject("PHYS");
        alt1.setNumber(150);
        alt1.setName("Physics I");
        alt1.setSection("03");
        alt1.setOpen_seats(1);
        alt1.setTotal_seats(30);
        alt1.setFaculty(new ArrayList<>());
        ClassTime alt1Time = new ClassTime();
        alt1Time.setDay("Monday");
        alt1Time.setStart_time("09:15:00");
        alt1Time.setEnd_time("10:45:00");
        List<ClassTime> alt1Times = new ArrayList<>();
        alt1Times.add(alt1Time);
        alt1.setTimes(alt1Times);

        // Alternative 04 - no conflict
        Course alt2 = new Course();
        alt2.setSubject("PHYS");
        alt2.setNumber(150);
        alt2.setName("Physics I");
        alt2.setSection("04");
        alt2.setOpen_seats(1);
        alt2.setTotal_seats(30);
        alt2.setFaculty(new ArrayList<>());
        ClassTime alt2Time = new ClassTime();
        alt2Time.setDay("Tuesday");
        alt2Time.setStart_time("14:00:00");
        alt2Time.setEnd_time("15:30:00");
        List<ClassTime> alt2Times = new ArrayList<>();
        alt2Times.add(alt2Time);
        alt2.setTimes(alt2Times);

        // Alternative 05 - no conflict
        Course alt3 = new Course();
        alt3.setSubject("PHYS");
        alt3.setNumber(150);
        alt3.setName("Physics I");
        alt3.setSection("05");
        alt3.setOpen_seats(1);
        alt3.setTotal_seats(30);
        alt3.setFaculty(new ArrayList<>());
        ClassTime alt3Time = new ClassTime();
        alt3Time.setDay("Wednesday");
        alt3Time.setStart_time("11:00:00");
        alt3Time.setEnd_time("12:30:00");
        List<ClassTime> alt3Times = new ArrayList<>();
        alt3Times.add(alt3Time);
        alt3.setTimes(alt3Times);

        CourseCatalog catalog = new CourseCatalog();
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(targetCourse);
        courses.add(alt1);
        courses.add(alt2);
        courses.add(alt3);
        catalog.setClasses(courses);

        ArrayList<Course> alternatives = schedule.findAlternativeCourses(targetCourse, catalog);

        Assertions.assertEquals(2, alternatives.size());
        Assertions.assertTrue(alternatives.stream().anyMatch(c -> c.getSection().equals("04")));
        Assertions.assertTrue(alternatives.stream().anyMatch(c -> c.getSection().equals("05")));
    }

    @Test
    void findAlternativeCourses_ExcludesOriginalSection() {
        // Ensures the original course section is not included in alternatives
        Schedule schedule = new Schedule();

        // Target course
        Course targetCourse = new Course();
        targetCourse.setSubject("ENG");
        targetCourse.setNumber(101);
        targetCourse.setName("English Composition");
        targetCourse.setSection("01");
        targetCourse.setOpen_seats(1);
        targetCourse.setTotal_seats(30);
        targetCourse.setFaculty(new ArrayList<>());
        ClassTime targetTime = new ClassTime();
        targetTime.setDay("Monday");
        targetTime.setStart_time("09:00:00");
        targetTime.setEnd_time("10:30:00");
        List<ClassTime> targetTimes = new ArrayList<>();
        targetTimes.add(targetTime);
        targetCourse.setTimes(targetTimes);

        // Alternative section
        Course alternative = new Course();
        alternative.setSubject("ENG");
        alternative.setNumber(101);
        alternative.setName("English Composition");
        alternative.setSection("02");
        alternative.setOpen_seats(1);
        alternative.setTotal_seats(30);
        alternative.setFaculty(new ArrayList<>());
        ClassTime altTime = new ClassTime();
        altTime.setDay("Tuesday");
        altTime.setStart_time("11:00:00");
        altTime.setEnd_time("12:30:00");
        List<ClassTime> altTimes = new ArrayList<>();
        altTimes.add(altTime);
        alternative.setTimes(altTimes);

        CourseCatalog catalog = new CourseCatalog();
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(targetCourse);
        courses.add(alternative);
        catalog.setClasses(courses);

        ArrayList<Course> alternatives = schedule.findAlternativeCourses(targetCourse, catalog);

        Assertions.assertEquals(1, alternatives.size());
        Assertions.assertNotEquals("01", alternatives.get(0).getSection());
        Assertions.assertEquals("02", alternatives.get(0).getSection());
    }
}