package edu.gcc.intellijent;
import io.javalin.Javalin;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public class SearchController {

    private static Search search;

    public SearchController(Search search) {
        this.search = search;
    }


    public static void registerRoutes(Javalin app) {
        app.get("/search", ctx -> {
            String query = ctx.queryParam("query");
            String dept = ctx.queryParam("dept");
            String credits = ctx.queryParam("credits");
            String prof = ctx.queryParam("prof");
            String startTime = ctx.queryParam("startTime");
            String endTime = ctx.queryParam("endTime");
            String days = ctx.queryParam("days");
            String full = ctx.queryParam("isFull");

            if (query == null){
                query = "";
            }
            search.SetNewQuery(query);

            // Get results arraylist from search
            ArrayList<Course> results = search.GetResultList();

            if (dept != null) {
                DeptFilter filter = new DeptFilter();
                filter.deptCode = dept;
                results = filter.ApplyFilter(results);
            }

            if (credits != null) {
                CreditsFilter filter = new CreditsFilter();
                filter.credits = Integer.parseInt(credits);
                results = filter.ApplyFilter(results);
            }

            if (prof != null) {
                ProfFilter filter = new ProfFilter();
                //TODO add logic here to fit with the goofy way we're doing this
                filter.prof = new ArrayList<>();
                filter.prof.add(prof);
                results = filter.ApplyFilter(results);
            }

            if (days != null && startTime != null && endTime != null){
                DateTimeFilter filter = new DateTimeFilter();

                // DateTimeFilter needs a List of ClassTime objects.

                // First we must iterate through Days. We can split on commas.
                ArrayList<String> daysList = new ArrayList<>(List.of(days.split(",")));

                // NOTE: This may not be in any particular order. We need to reorder it before using it
                // Credit to the below code block goes to ChatGPT.

                // Define the correct order of weekdays
                List<String> order = Arrays.asList("M", "T", "W", "Th", "F");

                // Sort the list based on the index in the order list
                Collections.sort(daysList, new Comparator<String>() {
                    @Override
                    public int compare(String day1, String day2) {
                        return Integer.compare(order.indexOf(day1), order.indexOf(day2));
                    }
                });
                // End of generated code block.

                // ArrayList of ClassTime objects may now be created.
                ArrayList<ClassTime> filterTimes = new ArrayList<>();
                for(String day : daysList){
                    ClassTime c = new ClassTime();

                    c.setDay(day);
                    c.setStart_time(startTime);
                    c.setEnd_time(endTime);
                }

                filter.schedule = filterTimes;

                results = filter.ApplyFilter(results);
            }

            if (full != null) {
                FullFilter filter = new FullFilter();
                filter.courseIsFull = Boolean.parseBoolean(full);
                results = filter.ApplyFilter(results);
            }

            ctx.json(results);
        });
    }
}
