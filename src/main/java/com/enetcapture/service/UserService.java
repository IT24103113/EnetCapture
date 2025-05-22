package com.enetcapture.service;

import com.enetcapture.model.User;
import java.io.*;

public class UserService {
    private static UserService instance;
    private CustomArray<User> users;
    private final String dataFilePath;

    private UserService() {
        users = new CustomArray<>();
        dataFilePath = new File(getClass().getClassLoader().getResource("WEB-INF/users.txt") != null ?
                getClass().getClassLoader().getResource("WEB-INF/users.txt").getFile() :
                "webapps/enetcapture/WEB-INF/users.txt").getAbsolutePath();
        initializeFile();
        loadUsers();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private void initializeFile() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("UserService: Created users.txt at " + dataFilePath);
            } catch (IOException e) {
                System.err.println("UserService: Failed to create users.txt: " + e.getMessage());
            }
        }
    }

    private void loadUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3], parts[4]));
                } else {
                    System.err.println("UserService: Skipping malformed line in users.txt: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("UserService: Error loading users.txt: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                String userData = String.format("%s,%s,%s,%s,%s%n", user.getUsername(), user.getPassword(), user.getFullName(), user.getEmail(), user.getPhone());
                writer.write(userData);
            }
            writer.flush();
            System.out.println("UserService: Successfully saved users to " + dataFilePath);
        } catch (IOException e) {
            System.err.println("UserService: Error saving users.txt: " + e.getMessage());
        }
    }

    public synchronized boolean addUser(User user) {
        loadUsers();
        if (getUserByUsername(user.getUsername()) == null) {
            users.add(user);
            saveUsers();
            return true;
        }
        return false;
    }

    public synchronized boolean updateUser(User user) {
        loadUsers();
        User existingUser = getUserByUsername(user.getUsername());
        if (existingUser != null) {
            existingUser.setPassword(user.getPassword());
            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            saveUsers();
            return true;
        }
        return false;
    }

    public synchronized boolean deleteUser(String username) {
        loadUsers();
        User user = getUserByUsername(username);
        if (user != null) {
            boolean removed = users.removeByPredicate(u -> u.getUsername().equals(username));
            if (removed) {
                saveUsers();
            }
            return removed;
        }
        return false;
    }

    public User getUserByUsername(String username) {
        loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return users.get(i);
            }
        }
        return null;
    }

    public CustomArray<User> getAllUsers() {
        loadUsers();
        return users.copy();
    }

    public boolean authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}