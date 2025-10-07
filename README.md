Car Rental System
Description

The Car Rental System is a JavaFX-based desktop application built in NetBeans 25 for managing all aspects of a car rental business.
It allows management of customers, vehicles, rentals, invoices, and reports through a clean professional UI, demonstrating OOP principles, database integration, and modular design.

This project is ideal for demonstrating real-world application development using Java, MySQL, and JavaFX.

Features
Customer Management

Add, Edit, Delete customers

Search by Name, NIC, License No, Phone, Email

Blacklist option for problematic customers

Customer table view with ID, Name, NIC, License, Phone, Email, Status

Vehicle Management

Add, Edit, Delete vehicles

Track vehicle availability (Available / Rented)

Search and filter by type, registration, or availability

Manage vehicle maintenance records

Rentals & Transactions

Create new rentals for customers

Track rental duration and rental status

Automatic vehicle availability updates

Generate invoices for completed rentals

Reporting

Generate reports by customer, vehicle, rental, and status

Filter reports by date, status, or type

Helps monitor overall business operations

Authentication

Login and SignUp system

Role-based access via UserRole enum (Admin, Staff)

Secure authentication for users

UI / UX

Professional interface with header, side navigation, and tables

FXML layouts for all modules
| Layer           | Technology          |
| --------------- | ------------------- |
| Language        | Java 17             |
| UI              | JavaFX (FXML)       |
| IDE             | NetBeans 25         |
| Database        | MySQL (Connector/J) |
| Build / Package | Maven               |
| Version Control | Git / GitHub        |


Visual icons and images for better usability

Responsive UI elements for a better user experience


Database (Extended)

The Car Rental System Database is a relational database that stores all application data. It ensures data integrity, relationships, and easy retrieval for operations like rentals, maintenance, and reporting.

Database Name: carrental

| Table Name              | Description                                                                                         | Key Fields / Relationships                                                                                |
| ----------------------- | --------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------- |
| **customers**           | Stores all customer information.                                                                    | `customer_id` (PK), `name`, `nic`, `license_no`, `phone`, `email`, `status`                               |
| **vehicles**            | Maintains details about each vehicle in the fleet.                                                  | `vehicle_id` (PK), `model`, `registration_no`, `type`, `status`, `mileage`, `last_service`                |
| **rentals**             | Records rental transactions including start and end dates, and the associated customer and vehicle. | `rental_id` (PK), `customer_id` (FK), `vehicle_id` (FK), `start_date`, `end_date`, `total_cost`, `status` |
| **payments**            | Tracks all payments made by customers for rentals.                                                  | `payment_id` (PK), `rental_id` (FK), `payment_date`, `amount`, `payment_method`, `status`                 |
| **vehicle_maintenance** | Logs maintenance and service records for each vehicle.                                              | `maintenance_id` (PK), `vehicle_id` (FK), `service_date`, `details`, `cost`                               |
| **users**               | Stores system users (admins, staff) for authentication and access control.                          | `user_id` (PK), `username`, `password_hash`, `role`, `last_login`                                         |
| **audit_logs**          | Keeps track of important actions performed in the system for accountability.                        | `log_id` (PK), `user_id` (FK), `action`, `timestamp`                                                      |


Relationships & Notes

One-to-Many Relationships:

One customer can have multiple rentals.

One vehicle can have multiple rentals and maintenance records.

One rental can have multiple payments.

Data Integrity:

Foreign key constraints enforce valid links between tables.

Status fields (active, rented, available) track the current state of customers and vehicles.

Design Benefits:

Normalized structure for efficient storage

Easy report generation (revenue, vehicle usage, customer activity)

Supports scaling for larger fleets or multiple branches

RentACar/
├── lib/                           # External libraries (MySQL Connector/J)
│   └── mysql-connector-j-9.0.0.jar
├── src/
│   ├── main/java/com/mycompany/rentcar/
│   │   ├── carrental/model/enums/        # Status and role enums (Booking, Vehicle, Payment, UserRole)
│   │   ├── dao/                          # Data Access Objects (CustomerDAO, VehicleDAO, BookingDAO, etc.)
│   │   ├── util/                         # DBConnection and utility classes
│   │   ├── controllers/                  # UI controllers (CustomerController, VehicleController, RentalController, etc.)
│   │   ├── Main.java                      # Application entry point
│   │   ├── UIMain.java                    # UI loader
│   │   ├── Models (Customer.java, Vehicle.java, User.java, Invoice.java, VehicleMaintenance.java)
│   └── main/resources/com/mycompany/rentcar/
│       ├── FXML layouts (addCustomer.fxml, Vehicle.fxml, Main.fxml, Rental.fxml, etc.)
│       ├── Images/icons (car.jpg, logo.png, Show.png, VisibleEye.png, etc.)
├── pom.xml                               # Maven build file
├── nbactions.xml                          # NetBeans actions
├── README.md                              # This file
├── out/                                   # NetBeans compiled output (ignored)
├── target/                                # Maven output (ignored)
└── .gitignore                             # Files to ignore in Git


Author

Avishka D. Rajapaksha

GitHub: avishka-d-rajapaksha

Portfolio-ready professional project showcasing JavaFX, MySQL, and OOP



