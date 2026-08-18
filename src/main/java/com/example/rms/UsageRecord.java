package com.example.rms;

import java.time.LocalDateTime;

public class UsageRecord {
    private int id;
    private int resourceId;
    private int quantity;
    private int people;
    private LocalDateTime startTime;
    private LocalDateTime plannedEndTime;
    private LocalDateTime actualEndTime;
    private double totalCost;
    private String status;

    public UsageRecord(int id, int resourceId, int quantity, int people,
                       LocalDateTime startTime, LocalDateTime plannedEndTime,
                       LocalDateTime actualEndTime, double totalCost, String status) {
        this.id = id;
        this.resourceId = resourceId;
        this.quantity = quantity;
        this.people = people;
        this.startTime = startTime;
        this.plannedEndTime = plannedEndTime;
        this.actualEndTime = actualEndTime;
        this.totalCost = totalCost;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPeople() {
        return people;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getPlannedEndTime() {
        return plannedEndTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getStatus() {
        return status;
    }
}
