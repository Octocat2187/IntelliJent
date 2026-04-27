package edu.gcc.intellijent;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.io.InputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {

            // Create Jackson mapper
            ObjectMapper mapper = new ObjectMapper();

            // Load Courses from Supabase via REST API
            CourseCatalog courseCatalog = SupabaseClient.getCoursesFromSupabase();

            List<Course> courses = courseCatalog.getClasses();

            Search search = new Search(courseCatalog, "");

            SearchController controller = new SearchController(search);

            Javalin app = Javalin.create(config -> {
                // Serve static files from: src/main/resources/public
//                config.staticFiles.add("public");

                // Enable CORS (allow requests from React dev server)
                config.bundledPlugins.enableCors(cors -> {
                    cors.addRule(it -> {
                        it.anyHost();
                    });
                });

            }).start(7000);

            controller.registerRoutes(app);
            UserScheduleStore userScheduleStore = new UserScheduleStore();
            ScheduleController.registerRoutes(app, courseCatalog, userScheduleStore);

            // Load Major JSON
            InputStream majorInput = Main.class.getClassLoader().getResourceAsStream("majors.json");
            MajorCatalog majorCatalog = mapper.readValue(majorInput, MajorCatalog.class);
            MajorController majorController = new MajorController(majorCatalog);
            majorController.registerRoutes(app);

            AccountStore accountStore = new AccountStore();
            AccountController accountController = new AccountController(accountStore, userScheduleStore);
            accountController.registerRoutes(app);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}