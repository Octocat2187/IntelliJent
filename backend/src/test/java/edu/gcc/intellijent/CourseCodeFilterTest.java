package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CourseCodeFilterTest {
    @Test
    void filterFail() {
        Course basketWeaving = new Course();
        basketWeaving.setCourseCode(111);

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        CourseCodeFilter filter = new CourseCodeFilter();
        filter.courseCode =  -1;
        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);

        Assertions.assertNotEquals(testCourses, filteredResults);
    }

    @Test
    void filterApply(){
        Course basketWeaving = new Course();
        basketWeaving.setCourseCode(111);

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        CourseCodeFilter filter = new CourseCodeFilter();
        filter.courseCode = 111;
        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);

        Assertions.assertEquals(testCourses, filteredResults);
    }
}