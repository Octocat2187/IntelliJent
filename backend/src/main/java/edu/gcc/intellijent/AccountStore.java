package edu.gcc.intellijent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccountStore {
    private final Map<String, Account> accounts = new HashMap<>();
    private final File file = new File("accounts.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public AccountStore() {
        loadFromFile();
        if (!accounts.containsKey("admin")) {
            createAccount("admin", "admin123", "");
        }
    }

    private void loadFromFile() {
        try {
            if (file.exists()) {
                Map<String, Account> data =
                        mapper.readValue(file, new TypeReference<Map<String, Account>>() {});
                accounts.putAll(data);
            } else {
                saveToFile();
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, accounts);
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    public boolean validateLogin(String username, String password) {
        return accounts.containsKey(username) &&
                accounts.get(username).getPassword().equals(password);
    }

    public boolean userExists(String username) {
        return accounts.containsKey(username);
    }

    public boolean createAccount(String username, String password, String major) {
        if (accounts.containsKey(username)) return false;

        Account acc = new Account();
        acc.setUsername(username);
        acc.setPassword(password);
        acc.setMajor(major);

        accounts.put(username, acc);
        saveToFile();
        return true;
    }

    public Account getAccount(String username) {
        return accounts.get(username);
    }

    public boolean changeMajor(String username, String newMajor) {
        Account acc = accounts.get(username);
        if (acc == null) return false;

        acc.setMajor(newMajor);
        saveToFile();
        return true;
    }
    public Set<String> getAllUsernames() {
        return accounts.keySet();
    }
    public boolean deleteUser(String username) {
        boolean removed = accounts.remove(username) != null;
        if (removed) saveToFile();
        return removed;
    }
}