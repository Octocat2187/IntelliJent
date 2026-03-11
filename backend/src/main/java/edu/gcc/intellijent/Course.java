package edu.gcc.intellijent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    //Class Variables
    private String subject;
    private int number;
    private String name;
    private String section;
    private String location;
    private List<String> faculty;
    private int credits;
    private int open_seats;
    private int total_seats;
    private boolean is_open;
    private List<ClassTime> times;
    private String daysOfWeek;

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

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getFaculty() {
        return faculty;
    }

    public void setFaculty(List<String> faculty) {
        this.faculty = faculty;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getOpen_seats() {
        return open_seats;
    }

    public void setOpen_seats(int open_seats) {
        this.open_seats = open_seats;
    }

    public int getTotal_seats() {
        return total_seats;
    }

    public void setTotal_seats(int total_seats) {
        this.total_seats = total_seats;
    }

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public List<ClassTime> getTimes() {
        return times;
    }

    public void setTimes(List<ClassTime> times) {
        this.times = times;
    }

    public boolean isAvailable(){
        //Returns true if the course is not full, false otherwise
        return open_seats > 0;
    }

    public void viewCourseInfo(){
        //Returns all the course information

    }

    public String getCourseCode()   {
        return subject + number;
    }
}
