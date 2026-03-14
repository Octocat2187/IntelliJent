package edu.gcc.intellijent;
import java.util.ArrayList;
import java.util.List;

public class Search {

    //Class Variables
    private CourseCatalog catalog;
    private ArrayList<Course> resultList;
    private Filter currFilter;
    private String searchQuery;


    public Search(CourseCatalog catalog){
        this.catalog = catalog;
    }

    public ArrayList<Course> GetResultList(){
        return resultList;
    }

    public void CourseSearch(String query) {
        searchQuery = query;
        if (query == null){
            resultList = (ArrayList<Course>) catalog.getClasses();
            return;
        }
        resultList = new ArrayList<Course>();
        List<Course> courses = catalog.getClasses();
        for (Course course : courses) {
            String courseCode = course.getSubject() + course.getNumber();
            if (courseCode.equals(searchQuery)) {
                resultList.add(course);
            }
        }
    }
}
