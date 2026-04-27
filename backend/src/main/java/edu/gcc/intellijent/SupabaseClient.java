package edu.gcc.intellijent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SupabaseClient {
    private static final String SUPABASE_URL = "https://fibkcgtknjlzhskzcquy.supabase.co";
    private static final String SUPABASE_API_KEY = "sb_publishable_Eqii8K9RWI7NTU6JehHjGQ__jWrImo8";
    private static final String TABLE_NAME = "courses";
    
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static CourseCatalog getCoursesFromSupabase() throws Exception {
        // Build the REST API URL for fetching all courses
        String url = String.format("%s/rest/v1/%s?select=*", SUPABASE_URL, TABLE_NAME);
        
        // Create HTTP request with Supabase API key
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_API_KEY)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        // Execute the request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            // Parse the JSON response as raw JSON nodes first
            JsonNode[] jsonNodes = objectMapper.readValue(response.body(), JsonNode[].class);
            
            List<Course> courses = new ArrayList<>();
            
            for (JsonNode node : jsonNodes) {
                Course course = parseCourseFromJsonNode(node);
                courses.add(course);
            }
            
            // Initialize empty lists for null fields
            for (Course course : courses) {
                if (course.getFaculty() == null) {
                    course.setFaculty(new ArrayList<>());
                }
                if (course.getTimes() == null) {
                    course.setTimes(new ArrayList<>());
                }
            }
            
            CourseCatalog catalog = new CourseCatalog();
            catalog.setClasses(courses);
            
            return catalog;
        } else {
            throw new Exception("Failed to fetch courses from Supabase. Status: " + response.statusCode() + 
                              ", Response: " + response.body());
        }
    }
    
    private static Course parseCourseFromJsonNode(JsonNode node) {
        Course course = new Course();
        
        // Simple fields
        course.setSubject(getStringValue(node, "subject"));
        course.setNumber(getIntValue(node, "number"));
        course.setName(getStringValue(node, "name"));
        course.setSection(getStringValue(node, "section"));
        course.setLocation(getStringValue(node, "location"));
        course.setCredits(getIntValue(node, "credits"));
        course.setOpen_seats(getIntValue(node, "open_seats"));
        course.setTotal_seats(getIntValue(node, "total_seats"));
        course.setIs_open(getBooleanValue(node, "is_open"));
        course.setSemester(getStringValue(node, "semester"));
        
        // Faculty list - use field names WITH NO SPACES (faculty/0, faculty/1, etc.)
        List<String> facultyList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String fieldName = "faculty/" + i;
            String profName = getStringValue(node, fieldName);
            if (profName != null && !profName.trim().isEmpty()) {
                facultyList.add(profName);
            }
        }
        course.setFaculty(facultyList);
        
        // Class times list - use field names WITH NO SPACES (times/0/day, times/0/start_time, etc.)
        List<ClassTime> times = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String dayField = "times/" + i + "/day";
            String startField = "times/" + i + "/start_time";
            String endField = "times/" + i + "/end_time";
            
            String day = getStringValue(node, dayField);
            if (day != null && !day.trim().isEmpty()) {
                ClassTime ct = new ClassTime();
                ct.setDay(day);
                ct.setStart_time(getStringValue(node, startField));
                ct.setEnd_time(getStringValue(node, endField));
                times.add(ct);
            }
        }
        course.setTimes(times);
        
        return course;
    }
    
    private static String getStringValue(JsonNode node, String fieldName) {
        if (node.has(fieldName)) {
            JsonNode value = node.get(fieldName);
            if (value.isNull()) return null;
            return value.asText();
        }
        return null;
    }
    
    private static int getIntValue(JsonNode node, String fieldName) {
        if (node.has(fieldName)) {
            JsonNode value = node.get(fieldName);
            if (value.isNull()) return 0;
            return value.asInt(0);
        }
        return 0;
    }
    
    private static boolean getBooleanValue(JsonNode node, String fieldName) {
        if (node.has(fieldName)) {
            JsonNode value = node.get(fieldName);
            if (value.isNull()) return false;
            return value.asBoolean(false);
        }
        return false;
    }
}



