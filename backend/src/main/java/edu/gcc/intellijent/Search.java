package edu.gcc.intellijent;
import java.util.ArrayList;

public class Search {

    //Class Variables
    private ArrayList<Course> resultList;
    private Filter currFilter;
    private String searchQuery;


    public Search(String query){
        return;
    }

    public ArrayList<Course> GetResultList(){
        return resultList;
    }

    private void CourseSearch() {
        resultList = new ArrayList<Course>();
    }
}
