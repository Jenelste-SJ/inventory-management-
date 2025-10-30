🛒 Inventory Management System
📘 Overview

The Inventory Management System is a Java-based console application that automates the process of managing products, tracking stock levels, generating reports, and alerting users when inventory levels are low.
It also includes secure authentication with email OTP verification and a reporting module to export data in CSV format.

🧩 Features

👤 User Authentication

Registration with Email Verification (via OTP)

Login restricted to verified users only

Role-based dashboards (Admin & User)

📦 Inventory Management

Add, Update, Delete, and View products

Search products by ID, Name, Category, or Price Range

📊 Reporting

Generate CSV reports of product inventory

Email report functionality with attachments

⚠️ Stock Alert System

Automatic email alerts for low-stock products

Configurable stock threshold levels

🧠 Additional Modules

CSV Helper for exporting reports

Email Utility for OTP & report delivery

OTP Service with in-memory verification

🧱 Project Architecture

Architecture Flow:

User → Authentication → Inventory Service → Database → CSV/Email Reports


Layers:

Model Layer: Contains Product and User classes.

DAO Layer: Handles all database operations via ProductDAOImpl and UserDAOImpl.

Service Layer: Contains business logic (InventoryService, UserService, StockAlertService, OTPService).

Util Layer: Provides helper utilities (DBConnection, CSVHelper, EmailUtil).

Main Application: Console-driven menus for Admin and User operations.

⚙️ Technology Stack

Language: Java 17+

Database: MySQL

Build Tool: Maven

IDE: IntelliJ IDEA

Testing: JUnit 5, Mockito

Utilities: CSV, SMTP Email API

🗂️ Database Schema

Table: products

Column	Type	Description
id	INT (PK)	Product ID
name	VARCHAR(100)	Product Name
category	VARCHAR(100)	Product Category
quantity	INT	Available Quantity
price	DOUBLE	Product Price
threshold	INT	Minimum stock threshold

Table: user

Column	Type	Description
id	INT (PK)	User ID
username	VARCHAR(100)	Unique username
password	VARCHAR(100)	Encrypted password (plain for demo)
role	VARCHAR(50)	Role (Admin/User)
email	VARCHAR(100)	Registered Email
isVerified	BOOLEAN	Email verification status
🧪 Testing

Unit tests implemented using JUnit 5 and Mockito.

Modules tested:

ProductDAOImplTest

UserDAOImplTest

DBConnectionTest

StockAlertServiceTest

OTPServiceTest

🧰 Setup & Run Instructions
🗄️ Prerequisites

Install Java 17+

Install MySQL and create a database (e.g., inventoryDatabase)

Install Maven

Set environment variables for DB and Mail:

DB_URL=jdbc:mysql://localhost:3306/inventoryDatabase
DB_USERNAME=root
DB_PASSWORD=yourpassword

MAIL_USER=youremail@gmail.com
MAIL_PASS=your-app-password

▶️ Run Project
# Clone Repository
git clone https://github.com/jenelsteSJ/inventory-management-system.git
cd inventory-management-system

# Build Project
mvn clean install

# Run Application
java -jar target/inventory-management-system.jar

🧭 How It Works

Register a new user → receives an OTP email.

Verify email using OTP → activates the account.

Login → access user or admin dashboard.

Admin can manage products and generate reports.

System sends automated alerts when stock is low.

📈 Future Enhancements

Web-based interface using Spring Boot and Thymeleaf/React.

Cloud-hosted deployment with AWS RDS and SMTP services.

Add data visualization dashboard for analytics.

👨‍💻 Author

Jenelste S J
📧 jenelste@gmail.com

🔗 GitHub: jenelsteSJ
