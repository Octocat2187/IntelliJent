package edu.gcc.intellijent;

import java.util.HashMap;
import java.util.Map;

public class UserScheduleStore {
    private final Map<String, Schedule> schedules = new HashMap<>();

    public Schedule getSchedule(String username) {
        return schedules.computeIfAbsent(username, k -> new Schedule());
    }

    public void clearSchedule(String username) {
        schedules.put(username, new Schedule());
    }
}