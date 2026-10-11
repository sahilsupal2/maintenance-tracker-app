# Maintenance Tracker App

A full-stack maintenance ticket tracking application consisting of a **Spring Boot** REST backend backed by **MariaDB**, and an **Android (Kotlin)** mobile client.

---

## 🛠 Tech Stack

* **Mobile App:** Kotlin, Android SDK (API 33), Retrofit2, Kotlin Coroutines, Material Design 3, RecyclerView
* **Backend:** Java 21, Spring Boot 4, Spring Data JPA, Hibernate, Maven
* **Database:** MariaDB (InnoDB dialect)

---

## ✨ Features

* **Real-time Ticket Feed (Screen 1):** Lists all reported maintenance tickets with status, category, ID, and description.
* **Instant Status Resolution:** Mark open tickets as `RESOLVED` directly from the list via a `PATCH` endpoint.
* **Ticket Submission (Screen 2):** Submit new tickets via a `POST` endpoint with automatic unit association.
* **Resident Onboarding:** Register new apartment units (`POST /api/units`) and persist assigned IDs locally with `SharedPreferences`.
* **Resilient UI States:** Handles loading spinners, empty ticket lists, and network offline/server failure with retry capability.

---

## 🗄️ Database Setup (MariaDB)

1. Start your local MariaDB / MySQL service:
   ```bash
   sudo systemctl start mariadb
   ```

2. Log into the database console:
   ```bash
   mariadb -u root -p
   ```

3. Create the database, tables, and seed data:
   ```sql
   CREATE DATABASE IF NOT EXISTS maintenance_db;
   USE maintenance_db;

   -- 1. Units Table
   CREATE TABLE IF NOT EXISTS units (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       unit_number VARCHAR(50) NOT NULL,
       resident_name VARCHAR(100) NOT NULL,
       phone_number VARCHAR(20) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- 2. Maintenance Tickets Table
   CREATE TABLE IF NOT EXISTS maintenance_tickets (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       unit_id BIGINT NOT NULL,
       category VARCHAR(50) NOT NULL,
       description TEXT,
       status VARCHAR(20) DEFAULT 'OPEN',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       CONSTRAINT fk_unit FOREIGN KEY (unit_id) REFERENCES units(id) ON DELETE CASCADE
   );

   -- 3. Initial Seed Data
   INSERT INTO units (unit_number, resident_name, phone_number) VALUES
   ('A-101', 'Rahul Sharma', '9876543210'),
   ('B-204', 'Priya Patel', '9876543211'),
   ('C-305', 'Amit Verma', '9876543212');

   INSERT INTO maintenance_tickets (unit_id, category, description, status) VALUES
   (1, 'Plumbing', 'Kitchen sink pipe is leaking water continuously.', 'RESOLVED'),
   (1, 'Electrical', 'Living room ceiling fan regulator not functioning.', 'RESOLVED'),
   (2, 'Carpentry', 'Main door lock gets jammed when closing.', 'OPEN'),
   (2, 'Electrical', 'Balcony bulb socket is sparking.', 'OPEN');
   ```

---

## 🚀 Backend Setup (Spring Boot)

1. Configure your database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/maintenance_db?user=root&password=YOUR_PASSWORD
   spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   server.port=8080
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   *The server starts on `http://localhost:8080`.*

---

## 📱 Mobile App Setup (Android)

1. Open the Android project in **Android Studio**.
2. Locate `network/ApiClient.kt` and update `BASE_URL` to match your development machine's local IP address:
   ```kotlin
   // Physical Android device (connected to the same Wi-Fi as host):
   private const val BASE_URL = "http://192.168.x.x:8080/"

   // Android Studio Emulator:
   // private const val BASE_URL = "http://10.0.2.2:8080/"
   ```
3. Run the app on a connected physical device or emulator.

---

## 🔌 API Endpoints Reference

| Method | Endpoint | Request Body | Description |
|---|---|---|---|
| `GET` | `/api/tickets` | None | Retrieves all maintenance tickets |
| `GET` | `/api/tickets/{id}` | None | Retrieves a single ticket by ID |
| `POST` | `/api/tickets` | `CreateTicketRequest` | Creates a new maintenance ticket |
| `PATCH` | `/api/tickets/{id}/status` | `UpdateStatusRequest` | Updates ticket status (`OPEN` / `RESOLVED`) |
| `DELETE` | `/api/tickets/{id}` | None | Deletes a ticket by ID |
| `GET` | `/api/units` | None | Lists all registered resident units |
| `POST` | `/api/units` | `CreateUnitRequest` | Registers a new unit and resident |
