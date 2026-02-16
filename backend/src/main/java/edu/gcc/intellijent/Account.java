package edu.gcc.intellijent;

public class Account {

    //THIS IS IAN BRANCH BECAUSE I SAID SO

    // STUB VARIABLES
    private Schedule courseSchedule;
    private Search currentSearch; // Suggested by Dr. Hutchins
    private String username;
    private String password;

    //----------------------------------------

    // STUB METHODS
    private void login(String username, String password){
        // I DON'T KNOW HOW TO LOGIN BUT WE SHOULD DEFINITELY HAVE THAT
    }

    private void AddCourse(Course course){
        // Add to schedule of account
    }

    private void RemoveCourse(Course course){
        // Remove from schedule of account
    }

    private void EditFilter(String dept, String code, String days, String times, String prof, Boolean full, int credits){

    }

    private void BeginSearch(String query){
        currentSearch = new Search(query);
    }

    private Schedule ViewSchedule(){
        return courseSchedule;
    }
}
