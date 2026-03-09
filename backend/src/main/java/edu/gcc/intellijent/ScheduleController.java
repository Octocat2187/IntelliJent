package edu.gcc.intellijent;
import io.javalin.Javalin;

import java.util.Map;

public class ScheduleController {
    private static final Schedule schedule = new Schedule();
    private static final Course example = new Course();

    static {
        // starter data so GET shows something
        schedule.AddCourse(example);
    }

    public static void registerRoutes(Javalin app) {

        app.get("/schedule", ctx -> ctx.json(schedule.Schedule));

        app.post("/courses", ctx -> {
            Course course = ctx.bodyAsClass(Course.class);
            schedule.AddCourse(course);
            ctx.status(201);  // 201 means “created”
        });

        app.delete("/courses", ctx -> {
            Course course = ctx.bodyAsClass(Course.class);
            boolean removed = schedule.RemoveCourse(course);

            if (removed) {
                ctx.status(204); // success, no body
            } else {
                ctx.status(404);
            }
        });

    }
}
