package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

class DateTimeFilterTest {

    @Test
    void filterFail(){
        Course basketWeaving = new Course();
        ClassTime classTime = new ClassTime();
        classTime.setDay("M");
        classTime.setStart_time("8:00:00");
        classTime.setEnd_time("9:00:00");

        List<ClassTime> classTimeList = new ArrayList<>();
        classTimeList.add(classTime);
        basketWeaving.setTimes(classTimeList);


        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        DateTimeFilter filter = new DateTimeFilter();

        ClassTime classTimeFail = new ClassTime();
        classTimeFail.setDay("Never Ever");
        classTimeFail.setStart_time("0:00:00");
        classTimeFail.setEnd_time("0:00:00");
        filter.schedule.add(classTimeFail);

        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);
        Assertions.assertNotEquals(testCourses, filteredResults);
    }

    @Test
    void filterPass(){
        Course basketWeaving = new Course();
        ClassTime classTime = new ClassTime();
        classTime.setDay("M");
        classTime.setStart_time("8:00:00");
        classTime.setEnd_time("9:00:00");

        List<ClassTime> classTimeList = new ArrayList<>();
        classTimeList.add(classTime);
        basketWeaving.setTimes(classTimeList);

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        DateTimeFilter filter = new DateTimeFilter();
        ClassTime filterTime = new ClassTime();
        filterTime.setDay("M");
        filterTime.setStart_time("7:00:00");
        filterTime.setEnd_time("11:00:00");
        filter.schedule.add(filterTime);

        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);
        Assertions.assertEquals(testCourses, filteredResults);
    }

    @Test
    void filterPassExact(){
        Course basketWeaving = new Course();
        ClassTime classTime = new ClassTime();
        classTime.setDay("M");
        classTime.setStart_time("8:00:00");
        classTime.setEnd_time("9:00:00");

        List<ClassTime> classTimeList = new ArrayList<>();
        classTimeList.add(classTime);
        basketWeaving.setTimes(classTimeList);

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        DateTimeFilter filter = new DateTimeFilter();
        filter.schedule.add(classTime);

        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);
        Assertions.assertEquals(testCourses, filteredResults);
    }

}