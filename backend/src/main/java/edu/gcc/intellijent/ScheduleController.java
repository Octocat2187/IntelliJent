package edu.gcc.intellijent;
import io.javalin.Javalin;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScheduleController {
    private static final Schedule schedule = new Schedule();
    private static CourseCatalog courseCatalog;


    public static void registerRoutes(Javalin app, CourseCatalog catalog) {
        courseCatalog = catalog;

        app.get("/schedule", ctx -> ctx.json(schedule.Schedule));

        app.post("/schedule", ctx -> {
            Course course = ctx.bodyAsClass(Course.class);
            schedule.AddCourse(course);
            if (schedule.isCourseAdded()){
                ctx.status(201);
                ctx.json(new CourseAddResponse(true, schedule.isCourseFull(), new java.util.ArrayList<>()));
            } else{
                java.util.ArrayList<Course> alternatives = schedule.findAlternativeCourses(course, courseCatalog);
                ctx.status(409);
                ctx.json(new CourseAddResponse(false, alternatives));
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

        app.post("/schedule/clear", ctx -> {
            schedule.clearSchedule();
            ctx.status(204);
        });

        app.post("/schedule/lucky", ctx -> {
            ObjectMapper mapper = new ObjectMapper();

            ArrayList<Course> courseList = mapper.readValue(
                    ctx.body(),
                    new TypeReference<ArrayList<Course>>() {}
            );

            ArrayList<Course> potentialList = new ArrayList<Course>();
            for (Course course : courseList){
                if (schedule.isCourseSchedulable(course)){
                    potentialList.add(course);
                }
            }
            Random rand = new Random();
            Course luckyCourse = potentialList.get(rand.nextInt(potentialList.size()));
            schedule.AddCourse(luckyCourse);
            if (schedule.isCourseAdded()){
                ctx.status(201);
            } else{
                ctx.status(409);
            }
        });
    }
}

