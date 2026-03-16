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

            // Load Course JSON
            InputStream courseInput = Main.class.getClassLoader().getResourceAsStream("allCourseInfo.json");

            CourseCatalog courseCatalog = mapper.readValue(courseInput, CourseCatalog.class);

            List<Course> courses = courseCatalog.getClasses();

            // Load Major JSON
            InputStream majorInput = Main.class.getClassLoader().getResourceAsStream("majors.json");

            MajorCatalog majorCatalog = mapper.readValue(majorInput, MajorCatalog.class);

            Scanner scanner = new Scanner(System.in);

            // Create account
            Account account = new Account("student", "password");

            // Select Major
            System.out.println("\nAvailable Majors:");

            for (Major m : majorCatalog.getMajors()) {
                System.out.println("- " + m.getName());
            }

            System.out.print("\nEnter your major: ");
            String selectedMajor = scanner.nextLine();

            for (Major m : majorCatalog.getMajors()) {
                if (m.getName().equalsIgnoreCase(selectedMajor)) {
                    account.setMajor(m);
                    break;
                }
            }

            // Display major requirements
            System.out.println("\nMajor Requirements:");
            account.viewMajorRequirements();

            // Course search
            System.out.print("\nEnter course code to search (ex: COMP141): ");
            String userInput = scanner.nextLine().toUpperCase();

            boolean found = false;

            for (Course course : courses) {

                String courseCode = course.getSubject() + course.getNumber();

                if (courseCode.equals(userInput)) {

                    System.out.println("\nCourse Name: " + course.getName());
                    System.out.println("Section: " + course.getSection());
                    System.out.println("Location: " + course.getLocation());
                    System.out.println("Professor: " + course.getFaculty());
                    System.out.println("Credits: " + course.getCredits());

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