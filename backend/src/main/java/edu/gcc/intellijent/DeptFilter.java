package edu.gcc.intellijent;
import java.util.ArrayList;

public class DeptFilter extends Filter {
    public String deptCode;

    @Override
    public ArrayList<Course> ApplyFilter(ArrayList<Course> searchResults) {
        ArrayList<Course> filteredResults = new ArrayList<>();
        for (Course course : searchResults) {
            if(course.GetDepartment().equals(deptCode)){
                filteredResults.add(course);
            }
        }

        return filteredResults;
    }
}
