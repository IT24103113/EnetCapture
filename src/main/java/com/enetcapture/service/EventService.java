package com.enetcapture.service;

import com.enetcapture.model.Event;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class EventService {
    private static EventService instance;
    private final String dataFilePath;
    private List<Event> events;
    private int nextId;

    private EventService() {
        events = new ArrayList<>();
        // Use classpath resource with fallback to default path
        dataFilePath = new File(getClass().getClassLoader().getResource("WEB-INF/events.txt") != null ?
                getClass().getClassLoader().getResource("WEB-INF/events.txt").getFile() :
                "webapps/enetcapture/WEB-INF/events.txt").getAbsolutePath();
        System.out.println("EventService: Initializing with file path: " + dataFilePath);
        initializeFile();
        loadEvents();
        nextId = events.stream().mapToInt(Event::getId).max().orElse(0) + 1;
    }

    public static synchronized EventService getInstance() {
        if (instance == null) instance = new EventService();
        return instance;
    }

    private void initializeFile() {
        File file = new File(dataFilePath);
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize events file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void loadEvents() {
        events.clear();
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.err.println("Events file does not exist: " + dataFilePath);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length == 9) {
                    events.add(new Event(
                            Integer.parseInt(p[0]), p[1], LocalDate.parse(p[2]), p[3], p[4],
                            Double.parseDouble(p[5]), p[6], p[7], p[8]
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading events: " + e.getMessage());
        }
    }

    private void saveEvents() {
        File file = new File(dataFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Event e : events) {
                writer.write(String.format("%d|%s|%s|%s|%s|%f|%s|%s|%s%n",
                        e.getId(), e.getName(), e.getDate(), e.getLocation(), e.getDescription(),
                        e.getBudget(), e.getSpecialInstructions(), e.getStatus(), e.getPaymentStatus()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving events: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized int addEvent(String name, LocalDate date, String location, String desc,
                                     double budget, String instr, String status, String payStatus) {
        loadEvents();
        Event e = new Event(nextId++, name, date, location, desc, budget, instr, status, payStatus);
        events.add(e);
        saveEvents();
        return e.getId();
    }

    public synchronized boolean updateEvent(int id, String name, LocalDate date, String location, String desc,
                                            double budget, String instr, String status, String payStatus) {
        loadEvents();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId() == id) {
                events.set(i, new Event(id, name, date, location, desc, budget, instr, status, payStatus));
                saveEvents();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteEvent(int id) {
        loadEvents();
        boolean removed = events.removeIf(e -> e.getId() == id);
        if (removed) saveEvents();
        return removed;
    }

    public Event getEventById(int id) {
        loadEvents();
        return events.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public List<Event> getAllEvents() {
        loadEvents();
        return new ArrayList<>(events);
    }
}