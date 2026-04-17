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
    }
}