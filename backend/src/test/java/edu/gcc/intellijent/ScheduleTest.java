package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



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
}