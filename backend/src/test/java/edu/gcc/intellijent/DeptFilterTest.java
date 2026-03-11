package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeptFilterTest {
    @Test
    void filterFail() {
        Course course = new Course();
        course.setDepartment("WEAV");

        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(course);

        DeptFilter deptFilter = new DeptFilter();
        deptFilter.deptCode = "Sneebs";

        ArrayList<Course> filteredResults = deptFilter.ApplyFilter(courses);
        Assertions.assertNotEquals(filteredResults, courses);
    }

    @Test
    void filterSuccess() {
        Course course = new Course();
        course.setDepartment("WEAV");

        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(course);

        DeptFilter deptFilter = new DeptFilter();
        deptFilter.deptCode = "WEAV";

        ArrayList<Course> filteredResults = deptFilter.ApplyFilter(courses);
        Assertions.assertEquals(filteredResults, courses);
    }


}