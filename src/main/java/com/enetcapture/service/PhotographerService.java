package com.enetcapture.service;

import com.enetcapture.model.Photographer;
import java.io.*;

public class PhotographerService {
    private static PhotographerService instance;
    private final String dataFilePath;
    private final CustomArray<Photographer> photographers;

    private PhotographerService() {
        photographers = new CustomArray<>();
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
            for (int i = 0; i < photographers.size(); i++) {
                Photographer photographer = photographers.get(i);
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
        // Convert CustomArray to a temporary array for easier swapping
        Photographer[] tempArray = photographers.toArray(new Photographer[0]);
        // Perform bubble sort on the temporary array
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (tempArray[j].getRating() < tempArray[j + 1].getRating()) {
                    // Swap elements in the temporary array
                    Photographer temp = tempArray[j];
                    tempArray[j] = tempArray[j + 1];
                    tempArray[j + 1] = temp;
                }
            }
        }
        // Clear the CustomArray and repopulate it with sorted elements
        photographers.clear();
        for (Photographer p : tempArray) {
            photographers.add(p);
        }
        System.out.println("PhotographerService: Photographers sorted by rating (descending).");
    }

    public synchronized boolean addPhotographer(String name, double rating) {
        loadPhotographers();
        if (name == null || name.trim().isEmpty() || rating < 0 || rating > 5) {
            return false;
        }
        for (int i = 0; i < photographers.size(); i++) {
            if (photographers.get(i).getName().equalsIgnoreCase(name)) {
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
        for (int i = 0; i < photographers.size(); i++) {
            if (photographers.get(i).getName().equalsIgnoreCase(name)) {
                photographers.get(i).setRating(newRating);
                sortByRating();
                savePhotographers();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deletePhotographer(String name) {
        loadPhotographers();
        boolean removed = photographers.removeByPredicate(p -> p.getName().equalsIgnoreCase(name));
        if (removed) {
            savePhotographers();
        }
        return removed;
    }

    public CustomArray<Photographer> getAllPhotographers() {
        loadPhotographers();
        return photographers.copy();
    }
}