package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeFilterTest {
    @Test
    void filterFail(){
        Course basketWeaving = new Course();
        basketWeaving.setStartTime(Time.valueOf("8:00:00"));
        basketWeaving.setEndTime(Time.valueOf("9:00:00"));
        basketWeaving.setDaysOfWeek("MWF");

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        DateTimeFilter filter = new DateTimeFilter();
        filter.beginTime = Time.valueOf("0:00:00");
        filter.endTime = Time.valueOf("0:00:00");
        filter.days = "Never ever";

        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);
        Assertions.assertNotEquals(testCourses, filteredResults);
    }

    @Test
    void filterPass(){
        Course basketWeaving = new Course();
        basketWeaving.setStartTime(Time.valueOf("8:00:00"));
        basketWeaving.setEndTime(Time.valueOf("9:00:00"));
        basketWeaving.setDaysOfWeek("MWF");

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        DateTimeFilter filter = new DateTimeFilter();
        filter.beginTime = Time.valueOf("7:00:00");
        filter.endTime = Time.valueOf("10:00:00");
        filter.days = "MWF";

        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);
        Assertions.assertEquals(testCourses, filteredResults);
    }

    @Test
    void filterPassExact(){
        Course basketWeaving = new Course();
        basketWeaving.setStartTime(Time.valueOf("8:00:00"));
        basketWeaving.setEndTime(Time.valueOf("9:00:00"));
        basketWeaving.setDaysOfWeek("MWF");

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        DateTimeFilter filter = new DateTimeFilter();
        filter.beginTime = Time.valueOf("8:00:00");
        filter.endTime = Time.valueOf("9:00:00");
        filter.days = "MWF";

        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);
        Assertions.assertEquals(testCourses, filteredResults);
    }

}