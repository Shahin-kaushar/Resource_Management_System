package com.example.rms;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final UsageRepository usageRepository;

    public ResourceService() {
        this.resourceRepository = new ResourceRepository();
        this.usageRepository = new UsageRepository();
    }

    public List<Resource> getResources() throws SQLException {
        return resourceRepository.findAll();
    }

    public double calculateCost(Resource resource, int quantity, int hours) {
        if (hours <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Hours and quantity must be greater than zero.");
        }

        double oneUnitCost =
                resource.getFirstHourPrice()
                + Math.max(0, hours - 1) * resource.getAdditionalHourPrice();

        return oneUnitCost * quantity;
    }

    public int roundUpHours(LocalDateTime start, LocalDateTime end) {
        long minutes = Duration.between(start, end).toMinutes();

        if (minutes <= 0) {
            return 1;
        }

        return (int) Math.ceil(minutes / 60.0);
    }

    public int startUsage(int resourceId, int quantity, int people, int requestedHours)
            throws SQLException {

        if (requestedHours <= 0) {
            throw new IllegalArgumentException("Duration must be at least 1 hour.");
        }

        Resource resource = resourceRepository.findById(resourceId);

        if (resource == null) {
            throw new IllegalArgumentException("Resource not found.");
        }

        validateRequest(resource, quantity, people);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(requestedHours);

        int used = usageRepository.getUsedQuantity(resourceId, start, end);

        if (used + quantity > resource.getCapacity()) {
            throw new IllegalStateException(
                    "Request rejected. Available capacity for this period: "
                    + (resource.getCapacity() - used)
            );
        }

        double cost = calculateCost(resource, quantity, requestedHours);

        return usageRepository.createUsage(
                resourceId,
                quantity,
                people,
                start,
                end,
                cost
        );
    }

    private void validateRequest(Resource resource, int quantity, int people) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        if (quantity > resource.getCapacity()) {
            throw new IllegalArgumentException(
                    "Requested quantity is greater than resource capacity."
            );
        }

        switch (resource.getCategory()) {
            case "MEETING" -> {
                if (people <= 0 || people > 15) {
                    throw new IllegalArgumentException(
                            "Meeting room supports a maximum of 15 people."
                    );
                }

                // A meeting always consumes the complete room.
                if (quantity != 1) {
                    throw new IllegalArgumentException(
                            "Meeting room quantity must be 1."
                    );
                }
            }

            case "CANTEEN" -> {
                if (people <= 0 || people > 25) {
                    throw new IllegalArgumentException(
                            "Canteen supports up to 25 people."
                    );
                }
                if (quantity != people) {
                    throw new IllegalArgumentException(
                            "For the canteen, quantity must equal number of people."
                    );
                }
            }

            case "QUIET" -> {
                if (people != 1 || quantity != 1) {
                    throw new IllegalArgumentException(
                            "Quiet room allows exactly one person."
                    );
                }
            }

            case "GYM_MACHINE" -> {
                if (quantity > 5) {
                    throw new IllegalArgumentException(
                            "Only 5 units of this machine are available."
                    );
                }
            }

            case "GYM_STRETCH" -> {
                if (quantity > 5) {
                    throw new IllegalArgumentException(
                            "Only 5 stretching spaces are available."
                    );
                }
            }

            case "PARKING_CAR" -> {
                if (quantity > 10) {
                    throw new IllegalArgumentException(
                            "Only 10 car parking spaces are available."
                    );
                }
            }

            case "PARKING_BIKE" -> {
                if (quantity > 5) {
                    throw new IllegalArgumentException(
                            "Only 5 bike/scooter parking spaces are available."
                    );
                }
            }

            default -> throw new IllegalArgumentException("Unknown resource category.");
        }
    }

    public void finishUsage(int usageId) throws SQLException {
        usageRepository.finishUsage(usageId);
    }

    public List<String> getUsageHistory() throws SQLException {
        return usageRepository.findAllUsage();
    }
}
