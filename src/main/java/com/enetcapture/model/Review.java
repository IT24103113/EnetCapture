package com.enetcapture.model;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private String username;
    private String photographer;
    private double rating;
    private String comment;
    private int eventId;
    private LocalDateTime date;

    public Review() {
        this.date = LocalDateTime.now();
    }

    public Review(int id, String username, String photographer, double rating, String comment, int eventId) {
        this.id = id;
        this.username = username;
        this.photographer = photographer;
        this.rating = rating;
        this.comment = comment;
        this.eventId = eventId;
        this.date = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPhotographer() { return photographer; }
    public void setPhotographer(String photographer) { this.photographer = photographer; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}