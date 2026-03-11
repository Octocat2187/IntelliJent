package edu.gcc.intellijent;

import java.sql.Time;

public class Course {
    //Class Variables
    private int courseCode;
    private String name;
    private int courseNumber;
    private String department;
    private String professor;
    private int numCredits;
    private boolean full;

    private String daysOfWeek;
    private Time startTime;
    private Time endTime;

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    //Getters and setters
    public int getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getNumCredits() {
        return numCredits;
    }

    public void setNumCredits(int numCredits) {
        this.numCredits = numCredits;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public boolean isAvailable(){
        //Returns true if the course is not full, false otherwise
        return true;
    }

    public void viewCourseInfo(){
        //Returns all the course information

    }
}
