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

            // Load Course JSON
            InputStream courseInput = Main.class.getClassLoader().getResourceAsStream("allCourseInfo.json");

            CourseCatalog courseCatalog = mapper.readValue(courseInput, CourseCatalog.class);

            List<Course> courses = courseCatalog.getClasses();

            Search search = new Search(courseCatalog, "");

            SearchController controller = new SearchController(search);

            Javalin app = Javalin.create(config -> {
                config.bundledPlugins.enableCors(cors -> {
                    cors.addRule(it -> {
                        it.anyHost();
                    });
                });
            }).start(7000);

            controller.registerRoutes(app);
            ScheduleController.registerRoutes(app, courseCatalog);

            InputStream majorInput = Main.class.getClassLoader().getResourceAsStream("majors.json");
            MajorCatalog majorCatalog = mapper.readValue(majorInput, MajorCatalog.class);
            Account account = new Account("student", "password");
            MajorController majorController = new MajorController(majorCatalog, account);
            majorController.registerRoutes(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}