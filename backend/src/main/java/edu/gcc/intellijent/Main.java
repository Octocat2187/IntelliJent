package edu.gcc.intellijent;

import io.javalin.Javalin;

public class Main {
    public static void run() {

        Javalin app = Javalin.create(config -> {
            // Serve static files from: src/main/resources/public
            config.staticFiles.add("public");

            // Enable CORS (allow requests from React dev server)
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });

        }).start(7000);

        SearchController.registerRoutes(app);
        ScheduleController.registerRoutes(app);
    }
}