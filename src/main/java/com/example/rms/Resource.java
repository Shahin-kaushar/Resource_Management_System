package com.example.rms;

public class Resource {
    private int id;
    private String name;
    private String category;
    private int capacity;
    private double firstHourPrice;
    private double additionalHourPrice;

    public Resource(int id, String name, String category, int capacity,
                    double firstHourPrice, double additionalHourPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.capacity = capacity;
        this.firstHourPrice = firstHourPrice;
        this.additionalHourPrice = additionalHourPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFirstHourPrice() {
        return firstHourPrice;
    }

    public double getAdditionalHourPrice() {
        return additionalHourPrice;
    }

    @Override
    public String toString() {
        return id + " - " + name +
                " | capacity=" + capacity +
                " | first hour=₹" + firstHourPrice +
                " | additional=₹" + additionalHourPrice;
    }
}
