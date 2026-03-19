package edu.gcc.intellijent;
import io.javalin.Javalin;

import java.util.Map;

public class ScheduleController {
    private static final Schedule schedule = new Schedule();


    public static void registerRoutes(Javalin app) {

        app.get("/schedule", ctx -> ctx.json(schedule.Schedule));

        app.post("/schedule", ctx -> {
            Course course = ctx.bodyAsClass(Course.class);
            schedule.AddCourse(course);
            if (schedule.isCourseAdded()){
                ctx.status(201);  // 201 means “created”
            } else{
                ctx.status(409);
            }
        });

        app.delete("/schedule", ctx -> {
            Course course = ctx.bodyAsClass(Course.class);
            //boolean removed = schedule.RemoveCourse(course);
            schedule.RemoveCourse(course);
            ctx.status(204);
//            if (removed) {
//                ctx.status(204); // success, no body
//            } else {
//                ctx.status(404);
//            }
        });

    }
}
