package edu.gcc.intellijent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CreditsFilterTest {
    @Test
    void filterFail(){
        Course basketWeaving = new Course();
        basketWeaving.setNumCredits(1);

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        CreditsFilter filter = new CreditsFilter();
        filter.credits = -1;

        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);

        Assertions.assertNotEquals(testCourses, filteredResults);
    }

    @Test
    void filterPass(){
        Course basketWeaving = new Course();
        basketWeaving.setNumCredits(1);

        ArrayList<Course> testCourses = new ArrayList<>();
        testCourses.add(basketWeaving);

        CreditsFilter filter = new CreditsFilter();
        filter.credits = 1;

        ArrayList<Course> filteredResults = filter.ApplyFilter(testCourses);

        Assertions.assertEquals(testCourses, filteredResults);
    }
}