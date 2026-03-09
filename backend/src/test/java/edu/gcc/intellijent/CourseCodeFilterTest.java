package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CourseCodeFilterTest {
    public static void main(String[] args) {
        Course basketWeaving = new Course();
        basketWeaving.setCourseCode("WEAV");

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        CourseCodeFilter filter = new CourseCodeFilter();
        filter.courseCode =  "Sneebs";
        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);

        Assertions.assertNotEquals(testCourses, filteredResults);

        filter.courseCode = "WEAV";
        filteredResults = filter.ApplyFilter(testCourses);

        Assertions.assertEquals(testCourses, filteredResults);
    }
}