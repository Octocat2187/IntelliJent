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
            String time = ctx.queryParam("time");
            String full = ctx.queryParam("isFull");

            Search search = new Search(query);
            // Get results arraylist from search
            ArrayList<Course> results = search.GetResultList();

            if (dept != null) {
                DeptFilter filter = new DeptFilter(dept);
                results = filter.ApplyFilter(results);
            }

            if (credits != null) {
                CreditsFilter filter = new CreditsFilter(Integer.parseInt(credits));
                results = filter.ApplyFilter(results);
            }

            if (prof != null) {
                ProfFilter filter = new ProfFilter(prof);
                results = filter.ApplyFilter(results);
            }

            if (time != null) {
                //TODO: the time variable will undoubtedly require some weird parsing
                ProfFilter filter = new DateTimeFilter(time);
                results = filter.ApplyFilter(results);
            }

            if (full != null) {
                FullFilter filter = new FullFilter(Boolean.parseBoolean(full));
                results = filter.ApplyFilter(results);
            }

            ctx.json(results);
        });
    }
}
