package edu.gcc.intellijent;

import java.util.HashMap;
import java.util.Map;

public class AccountStore {
    private final Map<String, String> accounts = new HashMap<>();

    public AccountStore() {
        // Temporary test accounts
        accounts.put("josh", "password123");
        accounts.put("admin", "admin123");
    }

    public boolean validateLogin(String username, String password) {
        return accounts.containsKey(username) && accounts.get(username).equals(password);
    }
}