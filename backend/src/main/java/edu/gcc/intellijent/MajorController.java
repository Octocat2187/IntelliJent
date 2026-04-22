package edu.gcc.intellijent;

import io.javalin.Javalin;

public class MajorController {

    private static MajorCatalog majorCatalog;

    public MajorController(MajorCatalog majorCatalog) {
        MajorController.majorCatalog = majorCatalog;
    }

    public static void registerRoutes(Javalin app) {
        app.get("/majors", ctx -> {
            ctx.json(majorCatalog.getMajors());
        });
    }
}