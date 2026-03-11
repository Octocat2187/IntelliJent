package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfFilterTest {
    @Test
    void filterFail(){
        Course course = new Course();
        course.setFaculty(new ArrayList<>(List.of("Dr. Straw")));

        ArrayList<Course> courses = new ArrayList<>();
        courses.add(course);

        ProfFilter profFilter = new ProfFilter();
        profFilter.prof = new ArrayList<>(List.of("Joe"));
        ArrayList<Course> filteredResults = profFilter.ApplyFilter(courses);

        Assertions.assertNotEquals(filteredResults, courses);
    }

    @Test
    void filterSuccess(){
        Course course = new Course();
        course.setFaculty(new ArrayList<String>(List.of("Dr. Straw")));

        ArrayList<Course> courses = new ArrayList<>();
        courses.add(course);

        ProfFilter profFilter = new ProfFilter();
        profFilter.prof = new ArrayList<String>(List.of("Dr. Straw"));
        ArrayList<Course> filteredResults = profFilter.ApplyFilter(courses);

        Assertions.assertEquals(filteredResults, courses);
    }
}