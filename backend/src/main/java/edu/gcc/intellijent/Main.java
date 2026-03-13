package edu.gcc.intellijent;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {

            // Create Jackson mapper
            ObjectMapper mapper = new ObjectMapper();

            // Load JSON from resources folder
            InputStream input = Main.class.getClassLoader()
                    .getResourceAsStream("allCourseInfo.json");

            // Convert JSON into CourseCatalog object
            CourseCatalog catalog = mapper.readValue(input, CourseCatalog.class);

            // Get list of courses
            List<Course> courses = catalog.getClasses();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter course code: ");
            String userInput = scanner.nextLine().toUpperCase();

            boolean found = false;

            // Search courses
            for (Course course : courses) {
                String courseCode = course.getSubject() + course.getNumber();
                if (courseCode.equals(userInput)) {
                    System.out.println("\nCourse Name: " + course.getName());
                    System.out.println("Section: " + course.getSection());
                    System.out.println("Location: " + course.getLocation());
                    System.out.println("Professor: " + course.getFaculty());
                    System.out.println("Credits: " + course.getCredits());

                    // Print meeting times
                    if (course.getTimes() != null) {
                        for (ClassTime time : course.getTimes()) {
                            System.out.println(
                                    "Time: " +
                                            time.getDay() + " " +
                                            time.getStart_time() + " - " +
                                            time.getEnd_time()
                            );
                        }
                    }
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Course not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}