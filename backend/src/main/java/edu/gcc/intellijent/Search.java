package edu.gcc.intellijent;
import java.util.ArrayList;

public class Search {

    //Class Variables
    private ArrayList<Course> resultList;
    private Filter currFilter;
    private String searchQuery;

    ArrayList<Filter> filterList;

    public Search(String query) {
        searchQuery = query;
    }

    public Search(String query, ArrayList<Filter> filterList) {
        searchQuery = query;
        this.filterList = filterList;

        CourseSearch();
    }

    private void CourseSearch() {
        resultList = new ArrayList<Course>();


    }
}
