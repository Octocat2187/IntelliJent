package edu.gcc.intellijent;
import io.javalin.Javalin;

import java.util.Map;

public class ScheduleController {
    private static CourseCatalog courseCatalog;
    private static UserScheduleStore userScheduleStore;

    public static void registerRoutes(Javalin app, CourseCatalog catalog, UserScheduleStore store) {
        courseCatalog = catalog;
        userScheduleStore = store;

        app.get("/schedule", ctx -> {
            String username = ctx.queryParam("username");

            if (username == null || username.isBlank()) {
                ctx.status(400).result("Username is required");
                return;
            }

            Schedule schedule = userScheduleStore.getSchedule(username);
            ctx.json(schedule.Schedule);
        });

        app.get("/roulette", ctx ->{
            String username =  ctx.queryParam("username");
            int guess = Integer.parseInt(ctx.queryParam("guess"));

            Schedule schedule = userScheduleStore.getSchedule(username);

            if(!schedule.wonRoulette(guess)){
                // you lose
                // delete schedule
                schedule.clearSchedule();

                ctx.status(204);
            }

            ctx.status(200);
        });

        app.post("/schedule", ctx -> {
            String username = ctx.queryParam("username");

            if (username == null || username.isBlank()) {
                ctx.status(400).json(java.util.Map.of("message", "Username is required"));
                return;
            }

            Schedule schedule = userScheduleStore.getSchedule(username);
            Course course = ctx.bodyAsClass(Course.class);

            schedule.AddCourse(course);

            if (schedule.isCourseAdded()) {
                ctx.status(201);
                ctx.json(new CourseAddResponse(true, schedule.isCourseFull(), new java.util.ArrayList<>()));
            } else {
                java.util.ArrayList<Course> alternatives = schedule.findAlternativeCourses(course, courseCatalog);
                ctx.status(409);
                ctx.json(new CourseAddResponse(false, alternatives));
            }
        });

        app.delete("/schedule", ctx -> {
            String username = ctx.queryParam("username");

            if (username == null || username.isBlank()) {
                ctx.status(400).result("Username is required");
                return;
            }

            Schedule schedule = userScheduleStore.getSchedule(username);
            Course course = ctx.bodyAsClass(Course.class);
            schedule.RemoveCourse(course);
            ctx.status(204);
        });

        app.post("/schedule/clear", ctx -> {
            String username = ctx.queryParam("username");

            if (username == null || username.isBlank()) {
                ctx.status(400).result("Username is required");
                return;
            }

            Schedule schedule = userScheduleStore.getSchedule(username);
            schedule.clearSchedule();
            ctx.status(204);
        });
    }
}

