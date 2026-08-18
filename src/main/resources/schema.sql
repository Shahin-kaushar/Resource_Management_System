CREATE DATABASE IF NOT EXISTS resource_management;
USE resource_management;

DROP TABLE IF EXISTS usage_records;
DROP TABLE IF EXISTS resources;

CREATE TABLE resources (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(30) NOT NULL,
    capacity INT NOT NULL,
    unit_price_first_hour DECIMAL(10,2) NOT NULL,
    unit_price_additional_hour DECIMAL(10,2) NOT NULL
);

CREATE TABLE usage_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    resource_id INT NOT NULL,
    quantity INT NOT NULL,
    start_time DATETIME NOT NULL,
    planned_end_time DATETIME NOT NULL,
    actual_end_time DATETIME NULL,
    people INT NOT NULL DEFAULT 1,
    total_cost DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (resource_id) REFERENCES resources(id)
);

INSERT INTO resources
(name, category, capacity, unit_price_first_hour, unit_price_additional_hour)
VALUES
('Meeting Room', 'MEETING', 15, 100, 50),

('Treadmill', 'GYM_MACHINE', 5, 50, 40),
('Stationary Bike', 'GYM_MACHINE', 5, 50, 40),
('Air Bike', 'GYM_MACHINE', 5, 50, 40),
('Row Machine', 'GYM_MACHINE', 5, 50, 40),
('Chest Press Machine', 'GYM_MACHINE', 5, 50, 40),
('Stretching Space', 'GYM_STRETCH', 5, 40, 30),

('Car Parking', 'PARKING_CAR', 10, 120, 100),
('Bike/Scooter Parking', 'PARKING_BIKE', 5, 90, 70),

('Canteen', 'CANTEEN', 25, 50, 40),
('Quiet Room', 'QUIET', 1, 200, 150);
