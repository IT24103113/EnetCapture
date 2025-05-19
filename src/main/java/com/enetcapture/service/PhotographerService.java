package com.enetcapture.service;

import com.enetcapture.model.Photographer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PhotographerService {
    private static PhotographerService instance;
    private final String dataFilePath;
    private final List<Photographer> photographers;

    private PhotographerService() {
        photographers = new ArrayList<>();
        dataFilePath = new File(getClass().getClassLoader().getResource("WEB-INF/photographers.txt") != null ?
                getClass().getClassLoader().getResource("WEB-INF/photographers.txt").getFile() :
                "webapps/enetcapture/WEB-INF/photographers.txt").getAbsolutePath();
        System.out.println("PhotographerService: Initializing with file path: " + dataFilePath);
        initializeFile();
        loadPhotographers();
    }

    public static synchronized PhotographerService getInstance() {
        if (instance == null) {
            instance = new PhotographerService();
        }
        return instance;
    }

    private void initializeFile() {
        File file = new File(dataFilePath);
        try {
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("PhotographerService: Created photographers.txt at " + dataFilePath);
            }
        } catch (IOException e) {
            System.err.println("PhotographerService: Failed to create photographers.txt: " + e.getMessage());
        }
    }

    private void loadPhotographers() {
        photographers.clear();
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.err.println("PhotographerService: photographers.txt does not exist at " + dataFilePath + ". Creating an empty list.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    photographers.add(new Photographer(parts[0].trim(), Double.parseDouble(parts[1].trim())));
                }
            }
            sortByRating();
            System.out.println("PhotographerService: Loaded " + photographers.size() + " photographers from " + dataFilePath);
        } catch (IOException e) {
            System.err.println("PhotographerService: Error loading photographers.txt: " + e.getMessage());
        }
    }

    private void savePhotographers() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            initializeFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
            for (Photographer photographer : photographers) {
                String photographerData = String.format("%s,%.1f%n", photographer.getName(), photographer.getRating());
                writer.write(photographerData);
            }
            writer.flush();
            System.out.println("PhotographerService: Successfully saved " + photographers.size() + " photographers to " + dataFilePath);
        } catch (IOException e) {
            System.err.println("PhotographerService: Error saving photographers.txt: " + e.getMessage());
        }
    }

    private void sortByRating() {
        int n = photographers.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (photographers.get(j).getRating() < photographers.get(j + 1).getRating()) {
                    Photographer temp = photographers.get(j);
                    photographers.set(j, photographers.get(j + 1));
                    photographers.set(j + 1, temp);
                }
            }
        }
        System.out.println("PhotographerService: Photographers sorted by rating (descending).");
    }

    public synchronized boolean addPhotographer(String name, double rating) {
        loadPhotographers();
        if (name == null || name.trim().isEmpty() || rating < 0 || rating > 5) {
            return false;
        }
        for (Photographer p : photographers) {
            if (p.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        photographers.add(new Photographer(name, rating));
        sortByRating();
        savePhotographers();
        return true;
    }

    public synchronized boolean updatePhotographer(String name, double newRating) {
        loadPhotographers();
        if (newRating < 0 || newRating > 5) {
            return false;
        }
        for (Photographer p : photographers) {
            if (p.getName().equalsIgnoreCase(name)) {
                p.setRating(newRating);
                sortByRating();
                savePhotographers();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deletePhotographer(String name) {
        loadPhotographers();
        boolean removed = photographers.removeIf(p -> p.getName().equalsIgnoreCase(name));
        if (removed) {
            savePhotographers();
        }
        return removed;
    }

    public List<Photographer> getAllPhotographers() {
        loadPhotographers();
        return new ArrayList<>(photographers);
    }
}