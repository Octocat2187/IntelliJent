package edu.gcc.intellijent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class CourseCatalog {
    private List<Course> classes;

    public List<Course> getClasses() {
        return classes;
    }

    public void setClasses(List<Course> classes) {
        this.classes = classes;
    }
}
