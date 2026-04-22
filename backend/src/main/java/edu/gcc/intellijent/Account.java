package edu.gcc.intellijent;

public class Account {

    //THIS IS IAN BRANCH BECAUSE I SAID SO

    // STUB VARIABLES
    private Schedule courseSchedule;
    private Search currentSearch; // Suggested by Dr. Hutchins
    private String username;
    private String password;
    private String major;

    public Account()    {

    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.courseSchedule = new Schedule();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    private void EditFilter(String dept, String code, String days, String times, String prof, Boolean full, int credits){

    }

    private void BeginSearch(String query){
        //currentSearch = new Search(query);
    }

    private Schedule ViewSchedule(){
        return courseSchedule;
    }

    // Display required courses for the major
//    public void viewMajorRequirements(){
//
//        if(major == null){
//            System.out.println("No major selected.");
//            return;
//        }
//
//        System.out.println("Major: " + major.getName());
//
//        for(String course : major.getRequiredCourses()){
//            System.out.println(course);
//        }
//    }
}