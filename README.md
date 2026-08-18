
# Resource Management System

A simple console-based **Resource Management System** built using **Java, Object-Oriented Programming (OOP), JDBC, and MySQL**.

The system manages meeting rooms, gym equipment, parking, canteen usage, and a quiet room. A user can start using a resource, finish the usage, and the system automatically calculates the bill from the actual usage time.

---

## Technologies Used

- Java 17+
- Object-Oriented Programming (OOP)
- JDBC
- MySQL 8+
- MySQL Connector/J
- Console-based interface

---

# Resources

## 1. Meeting Room

- Capacity: 15 people
- Only one meeting can use the room at a time.
- The entire room is considered occupied while a meeting is in progress.
- Requests for more than 15 people are rejected.

### Pricing

- First hour: ₹100
- Every additional hour: ₹50

---

## 2. Gym

The gym has a total capacity of 30 people.

### Equipment

| Equipment | Quantity |
|---|---:|
| Treadmills | 5 |
| Stationary Bikes | 5 |
| Air Bikes | 5 |
| Row Machines | 5 |
| Chest Press Machines | 5 |
| Stretching Space | 5 |

Gym machines are allocated independently.

### Machine Pricing

- First hour: ₹50 per machine
- Every additional hour: ₹40 per machine

### Stretching Space Pricing

- First hour: ₹40 per person
- Every additional hour: ₹30 per person

---

## 3. Parking Area

- Cars: 10
- Bikes/Scooters: 5

### Car Pricing

- First hour: ₹120
- Every additional hour: ₹100

### Bike/Scooter Pricing

- First hour: ₹90
- Every additional hour: ₹70

---

## 4. Canteen

- Capacity: 25 people
- Capacity is maintained per individual person.
- The canteen is for individual eating rather than reserving the whole area.

### Pricing

- First hour: ₹50 per person
- Every additional hour: ₹40 per person

---

## 5. Quiet Room

- Capacity: 1 person
- Only one person can use the room at a time.

### Pricing

- First hour: ₹200
- Every additional hour: ₹150

---

# Important Business Rules

- Resources have fixed capacities or quantities.
- A request is rejected when insufficient capacity is available.
- There is no future pre-booking.
- Usage starts immediately when the request is accepted.
- Start time is recorded automatically using the current time.
- Actual end time is recorded when the user finishes the resource usage.
- Billing duration is calculated from the actual start and end times.
- Usage time is rounded **UP** to the next complete hour.
- A final bill is generated when usage is finished.
- Completed usage information is stored in MySQL.
- Prices are in Indian Rupees (₹).

---

# Billing

The system calculates:

```text
Actual Duration = End Time - Start Time
Billed Hours    = Duration rounded UP to a whole hour
```

For example:

```text
Start Time: 10:00 AM
End Time:   11:20 AM

Actual Duration = 1 hour 20 minutes
Billed Duration = 2 hours
```

For the canteen:

```text
First hour      = ₹50
Additional hour = ₹40

Total = ₹90
```

The bill is generated immediately when the user finishes the resource usage.

---

# Project Structure

```text
resource_management_new/
│
├── lib/
│   └── mysql-connector-j-26.7.0/
│       └── mysql-connector-j-26.7.0.jar
│
├── out/
│   └── Compiled Java classes
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/rms/
│       │       ├── Main.java
│       │       ├── DBConnection.java
│       │       ├── Resource.java
│       │       ├── ResourceRepository.java
│       │       ├── ResourceService.java
│       │       ├── UsageRecord.java
│       │       └── UsageRepository.java
│       │
│       └── resources/
│           └── schema.sql
│
├── README.md
└── .gitignore
```

---

# Java Classes

## Main.java

The entry point of the application.

It provides the console menu and handles user input.

```text
1. Show resources
2. Start resource usage
3. Finish resource usage
4. Show usage history
5. Exit
```

`Main.java` mainly handles user interaction and calls the service layer.

## Resource.java

Represents a resource as a Java object.

It contains information such as:

- Resource ID
- Name
- Type
- Capacity or quantity
- Pricing information

## UsageRecord.java

Represents one usage transaction.

It contains:

- Usage ID
- Resource ID
- Quantity
- Start time
- End time
- Billed hours
- Total cost
- Status

## ResourceService.java

Contains the main business logic:

- Availability checking
- Capacity validation
- Starting usage
- Finishing usage
- Duration calculation
- Hour rounding
- Price calculation
- Bill generation

## ResourceRepository.java

Handles database operations related to resources, such as retrieving resources and finding a resource by ID.

## UsageRepository.java

Handles database operations related to usage records, such as creating usage, finishing usage, saving bills, and retrieving history.

## DBConnection.java

Creates the JDBC connection to MySQL.

The application connects to:

```text
Host: localhost
Port: 3306
Database: resource_management
```

---

# Application Architecture

```text
                 USER
                   |
                   v
              Main.java
             Console UI
                   |
                   v
          ResourceService
          Business Logic
             /       \
            /         \
           v           v
ResourceRepository  UsageRepository
           |           |
           +-----+-----+
                 |
                 v
              MySQL
```

`DBConnection.java` provides the JDBC connection used by the repositories.

---

# MySQL Setup

## 1. Create the database

Open MySQL Workbench and run:

```sql
CREATE DATABASE IF NOT EXISTS resource_management;
```

Then:

```sql
USE resource_management;
```

## 2. Run schema.sql

Open:

```text
src/main/resources/schema.sql
```

Run the SQL statements in MySQL Workbench.

Verify the tables:

```sql
SHOW TABLES;
```

---

# Configure MySQL

Open:

```text
src/main/java/com/example/rms/DBConnection.java
```

Set your MySQL username and password.

# MySQL Connector/J

Because Maven is not being used, the MySQL JDBC driver is added manually.

The project contains:

```text
lib/
└── mysql-connector-j-26.7.0/
    └── mysql-connector-j-26.7.0.jar
```

The connector provides the JDBC driver that allows Java to communicate with MySQL.

---

# Requirements

- JDK 17 or later
- MySQL 8 or later
- MySQL Workbench (recommended)
- VS Code, IntelliJ IDEA, or another Java editor
- MySQL Connector/J



# Compile the Project

Open PowerShell in the project root:

```text
C:\Users\<your-name>\Desktop\resource_management_new
```

Compile all Java files:

```powershell
javac -cp "lib\mysql-connector-j-26.7.0\mysql-connector-j-26.7.0.jar" -d out src\main\java\com\example\rms\*.java
```

If there is no error, compilation was successful.

---

# Run the Application

Run:

```powershell
java -cp "lib\mysql-connector-j-26.7.0\mysql-connector-j-26.7.0.jar;out" com.example.rms.Main
```

The application displays:

```text
======================================
 RESOURCE MANAGEMENT SYSTEM
======================================

1. Show resources
2. Start resource usage
3. Finish resource usage
4. Show usage history
5. Exit

Enter choice:
```

---

# Application Flow

## Start Resource Usage

Select:

```text
2. Start resource usage
```

The system:

1. Identifies the requested resource.
2. Checks availability.
3. Checks capacity or quantity.
4. Rejects the request if unavailable.
5. Records the current start time.
6. Creates an active usage record.
7. Saves it in MySQL.

There is no future booking.

## Finish Resource Usage

Select:

```text
3. Finish resource usage
```

The system:

1. Finds the active usage.
2. Records the actual end time.
3. Calculates the actual duration.
4. Rounds the duration UP to a complete hour.
5. Calculates the final cost.
6. Updates the usage record in MySQL.
7. Marks the usage as completed.

## Show Usage History

Select:

```text
4. Show usage history
```

The application retrieves previous usage records from MySQL.

---


# Future Improvements

Possible future improvements:

- REST API
- Postman testing
- Web-based interface
- Login and authentication
- Admin dashboard
- PDF receipt generation
- Resource maintenance status
- Reports and analytics
- Online payment integration

---

# Project Summary

This project demonstrates how **Java OOP, JDBC, and MySQL** can be combined to create a simple Resource Management System.

The main flow is:

```text
User
 |
 v
Main.java
 |
 v
ResourceService
 |
 +-------------------+
 |                   |
 v                   v
Repositories      Business Rules
 |
 v
MySQL
```

When a resource is started, the system records the start time.

When the user finishes the resource, the system records the actual end time, calculates the duration, rounds it up to the next hour, calculates the final price, generates the bill, and stores the completed transaction in MySQL.




