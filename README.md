
# Car Rental System

## Description
The Car Rental System is a **JavaFX-based desktop application** built in **NetBeans 25** for managing all aspects of a car rental business.  
It includes **customers, vehicles, rentals, invoices, and reports** with a **clean professional UI** and demonstrates **OOP, database integration, and modular design**.

---

## Features

### Customer Management
- Add, Edit, Delete customers
- Search by Name, NIC, License No, Phone, Email
- Blacklist option for problematic customers
- Customer table view with ID, Name, NIC, License, Phone, Email, Status

### Vehicle Management
- Add, Edit, Delete vehicles
- Track vehicle availability (Available / Rented)
- Search and filter by type, registration, or availability
- Manage vehicle maintenance records

### Rentals & Transactions
- Create new rentals for customers
- Track rental duration and rental status
- Automatic vehicle availability updates
- Generate invoices for completed rentals

### Reporting
- Generate reports by customer, vehicle, rental, and status
- Filter reports by date, status, or type
- Helps monitor overall business operations

### Authentication
- Login and SignUp system
- Role-based access via `UserRole` enum (Admin, Staff)
- Secure authentication for users

### UI / UX
- Professional interface with header, side navigation, and tables
- FXML layouts for all modules
- Visual icons and images for better usability
- Responsive UI elements for a better user experience

---

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| UI | JavaFX (FXML) |
| IDE | NetBeans 25 |
| Database | MySQL (Connector/J) |
| Build / Package | Maven |
| Version Control | Git / GitHub |

---

## Project Structure


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



---

## How to Run
1. Clone the repository:
```bash
git clone https://github.com/avishka-d-rajapaksha/CarRentalSystem.git
````

2. Open in **NetBeans 25**.
3. Build the project.
4. Run `Main.java`.
5. Login or SignUp and start managing **customers, vehicles, rentals, and reports**.

---

## Future Enhancements

* Integrate **online payment gateway**
* Export **reports to PDF/CSV**
* Implement **role-based permissions** for admin/staff
* Add **unit tests** for DAOs and controllers
* Add **theme switching / dark mode** for UI

---

## Author

**Avishka D. Rajapaksha**

* GitHub: [avishka-d-rajapaksha](https://github.com/avishka-d-rajapaksha)
* Portfolio-ready professional project showcasing JavaFX, MySQL, and OOP

---

````

---


````

---


Do you want me to do that?
