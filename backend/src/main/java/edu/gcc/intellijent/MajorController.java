package edu.gcc.intellijent;

import io.javalin.Javalin;

public class MajorController {

    private static MajorCatalog majorCatalog;
    private static Account account;

    public MajorController(MajorCatalog majorCatalog, Account account) {
        MajorController.majorCatalog = majorCatalog;
        MajorController.account = account;
    }

    public static void registerRoutes(Javalin app) {

        // Get all available majors
        app.get("/majors", ctx -> {
            ctx.json(majorCatalog.getMajors());
        });

        // Set the student's major
        app.post("/majors/select/{majorName}", ctx -> {
            String majorName = ctx.pathParam("majorName");

            for (Major m : majorCatalog.getMajors()) {
                if (m.getName().equalsIgnoreCase(majorName)) {
                    account.setMajor(m);
                    ctx.status(200);
                    ctx.json(m);
                    return;
                }
            }

            ctx.status(404);
            ctx.result("Major not found");
        });

        // Get required courses for the currently selected major
        app.get("/majors/requirements", ctx -> {
            if (account.getMajor() == null) {
                ctx.status(404);
                ctx.result("No major selected");
                return;
            }

            ctx.json(account.getMajor().getRequiredCourses());
        });
    }
}