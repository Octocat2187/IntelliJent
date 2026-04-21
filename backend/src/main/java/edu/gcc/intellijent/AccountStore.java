package edu.gcc.intellijent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccountStore {
    private final Map<String, String> accounts = new HashMap<>();
    private final File file = new File("accounts.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public AccountStore() {
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            if (file.exists()) {
                Map<String, String> data =
                        mapper.readValue(file, new TypeReference<Map<String, String>>() {});
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
        return accounts.containsKey(username) && accounts.get(username).equals(password);
    }

    public boolean userExists(String username) {
        return accounts.containsKey(username);
    }

    public boolean createAccount(String username, String password) {
        if (accounts.containsKey(username)) {
            return false;
        }

        accounts.put(username, password);
        saveToFile();
        return true;
    }
}