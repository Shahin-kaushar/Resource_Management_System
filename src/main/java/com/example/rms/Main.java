package com.example.rms;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ResourceService service = new ResourceService();

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println(" RESOURCE MANAGEMENT SYSTEM");
        System.out.println("======================================");

        while (true) {
            printMenu();

            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> showResources();
                    case 2 -> startUsage();
                    case 3 -> finishUsage();
                    case 4 -> showHistory();
                    case 5 -> {
                        System.out.println("Application closed.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Show resources");
        System.out.println("2. Start resource usage");
        System.out.println("3. Finish resource usage");
        System.out.println("4. Show usage history");
        System.out.println("5. Exit");
    }

    private static void showResources() throws SQLException {
        List<Resource> resources = service.getResources();

        System.out.println("\nAvailable resource definitions:");
        for (Resource resource : resources) {
            System.out.println(resource);
        }
    }

    private static void startUsage() throws SQLException {
        showResources();

        int resourceId = readInt("\nEnter resource ID: ");
        Resource resource = findResource(resourceId);

        if (resource == null) {
            System.out.println("Invalid resource ID.");
            return;
        }

        int people;

        if (resource.getCategory().equals("MEETING")) {
            people = readInt("Number of people (max 15): ");
        } else if (resource.getCategory().equals("CANTEEN")) {
            people = readInt("Number of people: ");
        } else if (resource.getCategory().equals("QUIET")) {
            people = 1;
        } else {
            people = readInt("Number of people using this resource: ");
        }

        int quantity;

        if (resource.getCategory().equals("MEETING")) {
            quantity = 1;
        } else if (resource.getCategory().equals("CANTEEN")) {
            quantity = people;
        } else if (resource.getCategory().equals("QUIET")) {
            quantity = 1;
        } else {
            quantity = readInt("Quantity/number of units: ");
        }

        int hours = readInt("How many hours will you use it? ");

        int usageId = service.startUsage(
                resourceId,
                quantity,
                people,
                hours
        );

        double cost = service.calculateCost(resource, quantity, hours);

        System.out.println("\nUsage started successfully.");
        System.out.println("Usage ID: " + usageId);
        System.out.println("Resource: " + resource.getName());
        System.out.println("Hours billed: " + hours);
        System.out.println("Estimated/current bill: ₹" + cost);
        System.out.println("Start time is the current time.");
        System.out.println("There is no future pre-booking.");
    }

    private static void finishUsage() throws SQLException {
        int usageId = readInt("Enter active usage ID to finish: ");
        service.finishUsage(usageId);
        System.out.println("Usage finished successfully.");
    }

    private static void showHistory() throws SQLException {
        List<String> history = service.getUsageHistory();

        if (history.isEmpty()) {
            System.out.println("No usage records found.");
            return;
        }

        System.out.println("\nUsage history:");
        for (String record : history) {
            System.out.println(record);
        }
    }

    private static Resource findResource(int id) throws SQLException {
        for (Resource resource : service.getResources()) {
            if (resource.getId() == id) {
                return resource;
            }
        }
        return null;
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
