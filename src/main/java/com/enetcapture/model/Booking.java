package com.enetcapture.model;

import java.time.LocalDate;

public class Booking {
    private int id;
    private String username;
    private String photographer;
    private LocalDate eventDate;
    private String eventType;
    private int eventId;

    public Booking() {}

    public Booking(int id, String username, String photographer, LocalDate eventDate, String eventType) {
        this.id = id;
        this.username = username;
        this.photographer = photographer;
        this.eventDate = eventDate;
        this.eventType = eventType;
    }

    public Booking(int id, String username, String photographer, LocalDate eventDate, String eventType, int eventId) {
        this.id = id;
        this.username = username;
        this.photographer = photographer;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.eventId = eventId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPhotographer() { return photographer; }
    public void setPhotographer(String photographer) { this.photographer = photographer; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return customIntHashCode(id);
    }

    private int customIntHashCode(int value) {
        return value;
    }
}