package edu.gcc.intellijent;
import java.util.ArrayList;

public class Search {

    //Class Variables
    private ArrayList<Course> resultList;
    private Filter currFilter;
    private String searchQuery;


    CourseCatalog courseCatalog;

    public Search(String query) {
        searchQuery = query;
    }

    public Search(CourseCatalog catalog, String query) {
        this.courseCatalog = catalog;
        searchQuery = query;

        CourseSearch();
    }

    private void CourseSearch() {
        resultList = new ArrayList<Course>();

        for (Course c : courseCatalog.getClasses()){
            boolean isMatch = false;

            if(c.getName().toLowerCase().contains(searchQuery.toLowerCase())){
                isMatch = true;
            }

            for(String faculty : c.getFaculty()){
                if(faculty.toLowerCase().contains(searchQuery.toLowerCase())){
                    isMatch = true;
                }
            }

            if(c.getSubject().toLowerCase().contains(searchQuery.toLowerCase())){
                isMatch = true;
            }

            if(c.getCourseCode().toLowerCase().contains(searchQuery.toLowerCase())){
                isMatch = true;
            }

            if(Integer.toString(c.getNumber()).contains(searchQuery.toLowerCase())){
                isMatch = true;
            }

            if(isMatch){
                resultList.add(c);
            }
        }



    }

    public ArrayList<Course> FilterResults(ArrayList<Filter> filterList) {
        ArrayList<Course> filteredResults = new ArrayList<>();
        for (Filter filter : filterList) {
            filteredResults = filter.ApplyFilter(resultList);
        }
        return filteredResults;
    }

}
