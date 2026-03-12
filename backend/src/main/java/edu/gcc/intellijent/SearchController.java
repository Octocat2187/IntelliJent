package edu.gcc.intellijent;
import io.javalin.Javalin;
import java.util.ArrayList;

public class SearchController {
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

            Search search = new Search(query);
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
                filter.prof = prof;
                results = filter.ApplyFilter(results);
            }

            if (days != null || startTime != null || endTime != null){
                DateTimeFilter filter = new DateTimeFilter();
                if (days != null){
                    filter.days = days;
                }
                if (startTime != null){
                    filter.beginTime = startTime;
                }
                if (endTime != null){
                    filter.endTime = endTime;
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
