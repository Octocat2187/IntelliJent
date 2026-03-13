package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FullFilterTest {
    @Test
    void filterFail(){
        Course course = new Course();
        course.setIs_open(true);

        ArrayList<Course> courses = new ArrayList<>();
        courses.add(course);

        FullFilter filter = new FullFilter();
        filter.courseIsFull = false;
        ArrayList<Course> filteredResults = filter.ApplyFilter(courses);

        Assertions.assertNotEquals(filteredResults, courses);
    }

    @Test
    void filterSuccess(){
        Course course = new Course();
        course.setIs_open(true);

        ArrayList<Course> courses = new ArrayList<>();
        courses.add(course);

        FullFilter filter = new FullFilter();
        filter.courseIsFull = true;
        ArrayList<Course> filteredResults = filter.ApplyFilter(courses);

        Assertions.assertEquals(filteredResults, courses);
    }
}