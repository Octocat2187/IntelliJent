package edu.gcc.intellijent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;

public class Search {

    //Class Variables
    private ArrayList<Course> resultList;
    private Filter currFilter;
    private String searchQuery;


    CourseCatalog courseCatalog;

    public Search(String query) {
        searchQuery = query;

        try{
            // Create Jackson mapper
            ObjectMapper mapper = new ObjectMapper();

            // Load Course JSON
            InputStream courseInput = Main.class.getClassLoader().getResourceAsStream("allCourseInfo.json");

            courseCatalog = mapper.readValue(courseInput, CourseCatalog.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Search(CourseCatalog catalog, String query) {
        this.courseCatalog = catalog;
        searchQuery = query;

        CourseSearch();
    }

    private void CourseSearch() {
        resultList = new ArrayList<Course>();

        for(Course c: courseCatalog.getClasses()){
            if(c.getSubject().toLowerCase().contains(searchQuery.toLowerCase())){
                if(!resultList.contains(c)){
                    resultList.add(c);
                }
            }
        }

        for (Course c : courseCatalog.getClasses()){
            if(c.getSubject().toLowerCase().contains(searchQuery.toLowerCase())){
                if(!resultList.contains(c)){
                    resultList.add(c);
                    continue;
                }
            }

            if(c.getName().toLowerCase().contains(searchQuery.toLowerCase())){
                if(!resultList.contains(c)){
                    resultList.add(c);
                    continue;
                }
            }

            boolean facultyFound = false;
            for(String faculty : c.getFaculty()){
                if(faculty.toLowerCase().contains(searchQuery.toLowerCase())){
                    facultyFound = true;
                }
            }
            if(facultyFound){
                if(!resultList.contains(c)){
                    resultList.add(c);
                    continue;
                }
            }

            if(c.getCourseCode().toLowerCase().contains(searchQuery.toLowerCase())){
                if(!resultList.contains(c)){
                    resultList.add(c);
                    continue;
                }
            }

            if(Integer.toString(c.getNumber()).contains(searchQuery.toLowerCase())){
                if(!resultList.contains(c)){
                    resultList.add(c);
                    continue;
                }
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

    public ArrayList<Course> GetResultList(){
        return resultList;
    }

    public void SetNewQuery(String query){
        searchQuery = query;

        CourseSearch();
    }

}
