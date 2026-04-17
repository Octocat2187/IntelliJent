package edu.gcc.intellijent;

import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;

public class AccountController {

    private static AccountStore accountStore;

    public AccountController(AccountStore accountStore) {
        AccountController.accountStore = accountStore;
    }

    public static void registerRoutes(Javalin app) {
        app.post("/login", ctx -> {
            Account account = ctx.bodyAsClass(Account.class);

            boolean valid = accountStore.validateLogin(account.getUsername(), account.getPassword());

            Map<String, Object> response = new HashMap<>();

            if (valid) {
                response.put("success", true);
                response.put("username", account.getUsername());
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

            accountStore.createAccount(username, password);

            response.put("success", true);
            response.put("username", username);
            response.put("message", "Account created successfully");
            ctx.status(201).json(response);
        });
    }
}