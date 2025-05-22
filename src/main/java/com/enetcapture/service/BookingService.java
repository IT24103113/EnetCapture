package com.enetcapture.service;

import com.enetcapture.model.Booking;
import java.io.*;

public class BookingService {
    private static BookingService instance;
    private final String dataFilePath;
    private final CustomQueue<Booking> bookings;

    private BookingService() {
        bookings = new CustomQueue<>();
        dataFilePath = new File(getClass().getClassLoader().getResource("WEB-INF/bookings.txt") != null ?
                getClass().getClassLoader().getResource("WEB-INF/bookings.txt").getFile() :
                "webapps/enetcapture/WEB-INF/bookings.txt").getAbsolutePath();
        System.out.println("BookingService: Initializing with file path: " + dataFilePath);
        initializeFile();
        loadBookings();
    }

    public static synchronized BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService();
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
                System.out.println("BookingService: Created bookings.txt at " + dataFilePath);
            }
        } catch (IOException e) {
            System.err.println("BookingService: Failed to create bookings.txt: " + e.getMessage());
        }
    }

    private void loadBookings() {
        bookings.clear();
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.err.println("BookingService: bookings.txt does not exist at " + dataFilePath + ". Creating an empty queue.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 6) { // Assuming Booking has fields: id, username, photographer, date, type, eventId
                    int id = Integer.parseInt(parts[0].trim());
                    String username = parts[1].trim();
                    String photographer = parts[2].trim();
                    String date = parts[3].trim();
                    String type = parts[4].trim();
                    int eventId = Integer.parseInt(parts[5].trim());
                    Booking booking = new Booking(id, username, photographer, java.time.LocalDate.parse(date), type, eventId);
                    bookings.enqueue(booking);
                }
            }
            System.out.println("BookingService: Loaded " + bookings.size() + " bookings from " + dataFilePath);
        } catch (IOException | NumberFormatException e) {
            System.err.println("BookingService: Error loading bookings.txt: " + e.getMessage());
        }
    }

    private void saveBookings() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            initializeFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
            CustomQueue<Booking> tempQueue = new CustomQueue<>();
            while (!bookings.isEmpty()) {
                Booking booking = bookings.dequeue();
                String bookingData = String.format("%d,%s,%s,%s,%s,%d%n",
                        booking.getId(), booking.getUsername(), booking.getPhotographer(),
                        booking.getEventDate().toString(), booking.getEventType(), booking.getEventId());
                writer.write(bookingData);
                tempQueue.enqueue(booking);
            }
            // Restore the queue
            while (!tempQueue.isEmpty()) {
                bookings.enqueue(tempQueue.dequeue());
            }
            writer.flush();
            System.out.println("BookingService: Successfully saved " + bookings.size() + " bookings to " + dataFilePath);
        } catch (IOException e) {
            System.err.println("BookingService: Error saving bookings.txt: " + e.getMessage());
        }
    }

    public synchronized boolean addBooking(Booking booking) {
        loadBookings();
        if (booking == null) return false;
        bookings.enqueue(booking);
        saveBookings();
        return true;
    }

    public synchronized boolean updateBooking(Booking booking) {
        loadBookings();
        if (booking == null || booking.getId() < 0) return false;
        CustomQueue<Booking> tempQueue = new CustomQueue<>();
        boolean updated = false;
        while (!bookings.isEmpty()) {
            Booking current = bookings.dequeue();
            if (current.getId() == booking.getId()) {
                tempQueue.enqueue(booking);
                updated = true;
            } else {
                tempQueue.enqueue(current);
            }
        }
        // Restore the queue
        while (!tempQueue.isEmpty()) {
            bookings.enqueue(tempQueue.dequeue());
        }
        if (updated) saveBookings();
        return updated;
    }

    public synchronized boolean deleteBooking(int id) {
        loadBookings();
        if (id < 0) return false;
        CustomQueue<Booking> tempQueue = new CustomQueue<>();
        boolean deleted = false;
        while (!bookings.isEmpty()) {
            Booking current = bookings.dequeue();
            if (current.getId() != id) {
                tempQueue.enqueue(current);
            } else {
                deleted = true;
            }
        }
        // Restore the queue
        while (!tempQueue.isEmpty()) {
            bookings.enqueue(tempQueue.dequeue());
        }
        if (deleted) saveBookings();
        return deleted;
    }

    public CustomQueue<Booking> getBookingsByUsername(String username) {
        loadBookings();
        CustomQueue<Booking> result = new CustomQueue<>();
        CustomQueue<Booking> tempQueue = new CustomQueue<>();
        while (!bookings.isEmpty()) {
            Booking current = bookings.dequeue();
            if (current.getUsername().equalsIgnoreCase(username)) {
                result.enqueue(current);
            }
            tempQueue.enqueue(current);
        }
        // Restore the original queue
        while (!tempQueue.isEmpty()) {
            bookings.enqueue(tempQueue.dequeue());
        }
        return result;
    }

    public CustomQueue<Booking> getAllBookings() {
        loadBookings();
        return bookings.copy(); // Assuming CustomQueue has a copy method similar to CustomArray
    }

    public Booking getBookingById(int id) {
        loadBookings();
        CustomQueue<Booking> tempQueue = new CustomQueue<>();
        Booking result = null;
        while (!bookings.isEmpty()) {
            Booking current = bookings.dequeue();
            if (current.getId() == id) {
                result = current;
            }
            tempQueue.enqueue(current);
        }
        // Restore the queue
        while (!tempQueue.isEmpty()) {
            bookings.enqueue(tempQueue.dequeue());
        }
        return result;
    }
}