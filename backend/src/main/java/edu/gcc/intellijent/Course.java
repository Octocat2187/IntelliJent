package edu.gcc.intellijent;
public class Course {
    //Class Variables
    private int courseCode;
    private String name;
    private int courseNumber;
    private String department;
    private String professor;
    private int numCredits;
    private boolean full;

    public boolean isAvailable(){
        //Returns true if the course is not full, false otherwise
        return true;
    }

    public void viewCourseInfo(){
        //Returns all the course information
    }
}
