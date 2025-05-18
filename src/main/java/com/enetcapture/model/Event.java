package com.enetcapture.model;

import java.time.LocalDate;

public class Event {
    private int id;
    private String name;
    private LocalDate date;
    private String location;
    private String description;
    private double budget;
    private String specialInstructions;
    private String status;
    private String paymentStatus;

    public Event() {}

    public Event(int id, String name, LocalDate date, String location, String description,
                 double budget, String specialInstructions, String status, String paymentStatus) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.budget = budget;
        this.specialInstructions = specialInstructions;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }


    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus= paymentStatus; }
}