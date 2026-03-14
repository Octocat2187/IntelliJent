package edu.gcc.intellijent;
import io.javalin.Javalin;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

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

            search.CourseSearch(query);
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

            if (days != null || startTime != null || endTime != null){
                DateTimeFilter filter = new DateTimeFilter();
                if (days != null){
                    filter.days = days;
                }
                if (startTime != null){
                    LocalTime localTime = LocalTime.parse(startTime);
                    Time start = Time.valueOf(localTime);
                    filter.beginTime = start;
                }
                if (endTime != null){
                    LocalTime localTime = LocalTime.parse(endTime);
                    Time end = Time.valueOf(localTime);
                    filter.endTime = end;
                }
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
