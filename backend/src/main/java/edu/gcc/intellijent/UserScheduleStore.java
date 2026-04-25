package edu.gcc.intellijent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserScheduleStore {

    private final Map<String, Schedule> schedules = new HashMap<>();
    private final File file = new File("schedules.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public UserScheduleStore() {
        loadFromFile();
    }

    public Schedule getSchedule(String username) {
        return schedules.computeIfAbsent(username, k -> {
            Schedule newSchedule = new Schedule();
            saveToFile();
            return newSchedule;
        });
    }

    public void saveUserSchedule(String username, Schedule schedule) {
        schedules.put(username, schedule);
        saveToFile();
    }

    public void clearSchedule(String username) {
        schedules.put(username, new Schedule());
        saveToFile();
    }

    private void loadFromFile() {
        try {
            if (file.exists()) {
                Map<String, Schedule> data =
                        mapper.readValue(file, new TypeReference<Map<String, Schedule>>() {});
                schedules.putAll(data);
            } else {
                saveToFile();
            }
        } catch (IOException e) {
            System.err.println("Error loading schedules:");
            e.printStackTrace();
        }
    }

    public void deleteUserSchedule(String username) {
        schedules.remove(username);
        saveToFile();
    }

    private void saveToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, schedules);
        } catch (IOException e) {
            System.err.println("Error saving schedules: " + e.getMessage());
        }
    }
}