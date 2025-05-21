package com.enetcapture.service;

import com.enetcapture.model.Event;
import java.io.*;
import java.time.LocalDate;

public class EventService {
    private static EventService instance;
    private final String dataFilePath;
    private CustomArray<Event> events;
    private int nextId;

    private EventService() {
        events = new CustomArray<>();
        dataFilePath = new File(getClass().getClassLoader().getResource("WEB-INF/events.txt") != null ?
                getClass().getClassLoader().getResource("WEB-INF/events.txt").getFile() :
                "webapps/enetcapture/WEB-INF/events.txt").getAbsolutePath();
        System.out.println("EventService: Initializing with file path: " + dataFilePath);
        initializeFile();
        loadEvents();
        int maxId = 0;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId() > maxId) {
                maxId = events.get(i).getId();
            }
        }
        nextId = maxId + 1;
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
            for (int i = 0; i < events.size(); i++) {
                Event e = events.get(i);
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
                events.get(i).setName(name);
                events.get(i).setDate(date);
                events.get(i).setLocation(location);
                events.get(i).setDescription(desc);
                events.get(i).setBudget(budget);
                events.get(i).setSpecialInstructions(instr);
                events.get(i).setStatus(status);
                events.get(i).setPaymentStatus(payStatus);
                saveEvents();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteEvent(int id) {
        loadEvents();
        boolean removed = events.removeByPredicate(e -> e.getId() == id);
        if (removed) saveEvents();
        return removed;
    }

    public Event getEventById(int id) {
        loadEvents();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId() == id) {
                return events.get(i);
            }
        }
        return null;
    }

    public CustomArray<Event> getAllEvents() {
        loadEvents();
        return events.copy();
    }
}