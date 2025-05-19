package com.enetcapture.service;

import com.enetcapture.model.Booking;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private static BookingService instance;
    private final String dataFilePath;
    private List<Booking> bookings;
    private int nextId;

    private BookingService() {
        bookings = new ArrayList<>();
        dataFilePath = new File(getClass().getClassLoader().getResource("WEB-INF/bookings.txt") != null ?
                getClass().getClassLoader().getResource("WEB-INF/bookings.txt").getFile() :
                "webapps/enetcapture/WEB-INF/bookings.txt").getAbsolutePath();
        System.out.println("BookingService: Initializing with file path: " + dataFilePath);
        initializeFile();
        loadBookings();
        nextId = bookings.stream().mapToInt(Booking::getId).max().orElse(0) + 1;
    }

    public static synchronized BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService();
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
                System.out.println("BookingService: Created bookings.txt at " + dataFilePath);
            } catch (IOException e) {
                System.err.println("BookingService: IOException while creating bookings.txt: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to create bookings.txt: " + e.getMessage());
            }
        } else {
            System.out.println("BookingService: bookings.txt already exists at " + dataFilePath);
        }
    }

    private void loadBookings() {
        bookings.clear();
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.err.println("BookingService: bookings.txt does not exist at " + dataFilePath + ". Creating an empty list.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    bookings.add(new Booking(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            LocalDate.parse(parts[3]),
                            parts[4],
                            Integer.parseInt(parts[5])
                    ));
                } else {
                    System.err.println("BookingService: Skipping malformed line in bookings.txt: " + line);
                }
            }
            System.out.println("BookingService: Loaded " + bookings.size() + " bookings from " + dataFilePath);
        } catch (IOException e) {
            System.err.println("BookingService: Error loading bookings.txt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveBookings() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            initializeFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
            for (Booking booking : bookings) {
                String bookingData = String.format("%d|%s|%s|%s|%s|%d%n",
                        booking.getId(), booking.getUsername(), booking.getPhotographer(), booking.getEventDate(),
                        booking.getEventType(), booking.getEventId());
                writer.write(bookingData);
            }
            writer.flush();
            System.out.println("BookingService: Successfully saved " + bookings.size() + " bookings to " + dataFilePath + " at " + new java.util.Date());
        } catch (IOException e) {
            System.err.println("BookingService: Error saving bookings.txt: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save bookings to file: " + e.getMessage());
        }
    }

    public synchronized boolean addBooking(Booking booking) {
        loadBookings();
        if (booking.getUsername() == null || booking.getPhotographer() == null || booking.getEventDate() == null ||
                booking.getEventType() == null) {
            System.err.println("BookingService: Invalid booking data for user " + booking.getUsername());
            return false;
        }
        booking.setId(nextId++);
        bookings.add(booking);
        saveBookings();
        System.out.println("BookingService: Added booking for user " + booking.getUsername() + " with ID " + booking.getId() + " at " + new java.util.Date());
        return true;
    }

    public synchronized boolean updateBooking(Booking booking) {
        loadBookings();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId() == booking.getId()) {
                bookings.set(i, booking);
                saveBookings();
                System.out.println("BookingService: Updated booking ID " + booking.getId() + " at " + new java.util.Date());
                return true;
            }
        }
        System.err.println("BookingService: Booking ID " + booking.getId() + " not found.");
        return false;
    }

    public synchronized boolean deleteBooking(int id) {
        loadBookings();
        boolean removed = bookings.removeIf(b -> b.getId() == id);
        if (removed) {
            saveBookings();
            System.out.println("BookingService: Deleted booking ID " + id + " at " + new java.util.Date());
        } else {
            System.err.println("BookingService: Booking ID " + id + " not found.");
        }
        return removed;
    }

    public Booking getBookingById(int id) {
        loadBookings();
        return bookings.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    public List<Booking> getBookingsByUsername(String username) {
        loadBookings();
        return bookings.stream().filter(b -> b.getUsername().equalsIgnoreCase(username)).toList();
    }

    public List<Booking> getAllBookings() {
        loadBookings();
        return new ArrayList<>(bookings);
    }
}