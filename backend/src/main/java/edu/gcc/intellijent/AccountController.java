package edu.gcc.intellijent;

import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;

public class AccountController {

    private static AccountStore accountStore;
    private static UserScheduleStore userScheduleStore;

    public AccountController(AccountStore accountStore, UserScheduleStore scheduleStore) {
        AccountController.accountStore = accountStore;
        AccountController.userScheduleStore = scheduleStore;
    }

    public static void registerRoutes(Javalin app) {
        app.post("/login", ctx -> {
            Account account = ctx.bodyAsClass(Account.class);

            boolean valid = accountStore.validateLogin(account.getUsername(), account.getPassword());

            Map<String, Object> response = new HashMap<>();


            if (valid) {
                boolean isAdmin = account.getUsername().equals("admin");
                response.put("success", true);
                response.put("username", account.getUsername());
                response.put("major", accountStore.getAccount(account.getUsername()).getMajor());
                response.put("isAdmin", isAdmin);
                ctx.status(200).json(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid username or password");
                ctx.status(401).json(response);
            }
        });

        app.post("/signup", ctx -> {
            Account account = ctx.bodyAsClass(Account.class);

            String username = account.getUsername();
            String password = account.getPassword();

            Map<String, Object> response = new HashMap<>();

            if (username == null || username.isBlank()) {
                response.put("success", false);
                response.put("message", "Username cannot be blank");
                ctx.status(400).json(response);
                return;
            }

            if (password == null || password.isBlank()) {
                response.put("success", false);
                response.put("message", "Password cannot be blank");
                ctx.status(400).json(response);
                return;
            }

            if (password.length() < 6) {
                response.put("success", false);
                response.put("message", "Password must be at least 6 characters");
                ctx.status(400).json(response);
                return;
            }

            if (accountStore.userExists(username)) {
                response.put("success", false);
                response.put("message", "Username already exists");
                ctx.status(409).json(response);
                return;
            }

            accountStore.createAccount(username, password, account.getMajor());

            response.put("success", true);
            response.put("username", username);
            response.put("message", "Account created successfully");
            ctx.status(201).json(response);
        });

        app.post("/changeMajor", ctx -> {
            Map<String, String> body = ctx.bodyAsClass(Map.class);

            String username = body.get("username");
            String newMajor = body.get("major");

            Map<String, Object> response = new HashMap<>();

            if (username == null || newMajor == null) {
                response.put("success", false);
                response.put("message", "Missing username or major");
                ctx.status(400).json(response);
                return;
            }

            boolean updated = accountStore.changeMajor(username, newMajor);

            if (!updated) {
                response.put("success", false);
                response.put("message", "User not found");
                ctx.status(404).json(response);
                return;
            }

            response.put("success", true);
            response.put("major", newMajor);
            ctx.status(200).json(response);
        });

        app.get("/users", ctx -> {

            String requester = ctx.queryParam("username");

            if (!"admin".equals(requester)) {
                ctx.status(403).result("Forbidden");
                return;
            }

            ctx.json(accountStore.getAllUsernames());
        });
        app.delete("/users/{username}", ctx -> {

            String requester = ctx.queryParam("username");
            String usernameToDelete = ctx.pathParam("username");

            if (!"admin".equals(requester)) {
                ctx.status(403).result("Forbidden");
                return;
            }

            if ("admin".equals(usernameToDelete)) {
                ctx.status(403).result("Cannot delete admin user");
                return;
            }

            boolean removed = accountStore.deleteUser(usernameToDelete);

            if (!removed) {
                ctx.status(404).result("User not found");
                return;
            }

            userScheduleStore.deleteUserSchedule(usernameToDelete);
            ctx.status(200).result("User deleted");
        });
    }
}