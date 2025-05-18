package com.enetcapture.service;

import com.enetcapture.model.Admin; // Updated import
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private static AdminService instance;
    private final String dataFilePath;
    private List<Admin> admins;

    private AdminService() {
        admins = new ArrayList<>();
        dataFilePath = new File(getClass().getClassLoader().getResource("WEB-INF/admins.txt") != null ?
                getClass().getClassLoader().getResource("WEB-INF/admins.txt").getFile() :
                "webapps/enetcapture/WEB-INF/admins.txt").getAbsolutePath();
        System.out.println("AdminService: Initializing with file path: " + dataFilePath);
        initializeFile();
        loadAdmins();
    }

    public static synchronized AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }
        return instance;
    }

    private void initializeFile() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
                System.out.println("AdminService: Created admins.txt at " + dataFilePath);
            } catch (IOException e) {
                System.err.println("AdminService: IOException while creating admins.txt: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to create admins.txt: " + e.getMessage());
            }
        } else {
            System.out.println("AdminService: admins.txt already exists at " + dataFilePath);
        }
    }

    private void loadAdmins() {
        admins.clear();
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.err.println("AdminService: admins.txt does not exist at " + dataFilePath + ". Creating an empty list.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    admins.add(new Admin(parts[0], parts[1]));
                } else {
                    System.err.println("AdminService: Skipping malformed line in admins.txt: " + line);
                }
            }
            System.out.println("AdminService: Loaded " + admins.size() + " admins from " + dataFilePath);
        } catch (IOException e) {
            System.err.println("AdminService: Error loading admins.txt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveAdmins() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            initializeFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
            for (Admin admin : admins) {
                String adminData = String.format("%s,%s%n", admin.getUsername(), admin.getPassword());
                writer.write(adminData);
            }
            writer.flush();
            System.out.println("AdminService: Successfully saved " + admins.size() + " admins to " + dataFilePath);
        } catch (IOException e) {
            System.err.println("AdminService: Error saving admins.txt: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save admins to file: " + e.getMessage());
        }
    }

    public synchronized boolean addAdmin(String username, String password) {
        loadAdmins();
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.err.println("AdminService: Invalid input - username and password cannot be empty.");
            return false;
        }
        for (Admin a : admins) {
            if (a.getUsername().equalsIgnoreCase(username)) {
                System.err.println("AdminService: Admin " + username + " already exists.");
                return false;
            }
        }
        admins.add(new Admin(username, password));
        saveAdmins();
        System.out.println("AdminService: Added admin: " + username);
        return true;
    }

    public synchronized boolean updateAdmin(String username, String newPassword) {
        loadAdmins();
        if (newPassword == null || newPassword.trim().isEmpty()) {
            System.err.println("AdminService: Password cannot be empty.");
            return false;
        }
        for (Admin a : admins) {
            if (a.getUsername().equalsIgnoreCase(username)) {
                a.setPassword(newPassword);
                saveAdmins();
                System.out.println("AdminService: Updated password for " + username);
                return true;
            }
        }
        System.err.println("AdminService: Admin " + username + " not found.");
        return false;
    }

    public synchronized boolean deleteAdmin(String username) {
        loadAdmins();
        boolean removed = admins.removeIf(a -> a.getUsername().equalsIgnoreCase(username));
        if (removed) {
            saveAdmins();
            System.out.println("AdminService: Deleted admin: " + username);
        } else {
            System.err.println("AdminService: Admin " + username + " not found.");
        }
        return removed;
    }

    public List<Admin> getAllAdmins() {
        loadAdmins();
        return new ArrayList<>(admins);
    }

    public boolean authenticate(String username, String password) {
        loadAdmins();
        for (Admin a : admins) {
            if (a.getUsername().equalsIgnoreCase(username) && a.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}