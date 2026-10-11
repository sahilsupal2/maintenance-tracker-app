-- Database Setup
CREATE DATABASE IF NOT EXISTS maintenance_db;
USE maintenance_db;

-- Tables
CREATE TABLE units (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_number VARCHAR(20) NOT NULL,
    resident_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE maintenance_tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tickets_unit FOREIGN KEY (unit_id) 
        REFERENCES units(id) 
        ON DELETE CASCADE
);

-- Seed Data
INSERT INTO units (unit_number, resident_name, phone_number) VALUES
('A-101', 'Rahul Sharma', '9876543210'),
('B-204', 'Priya Patel', '9876543211'),
('C-305', 'Amit Verma', '9876543212');

INSERT INTO maintenance_tickets (unit_id, category, description, status) VALUES
(1, 'Plumbing', 'Kitchen sink pipe is leaking water continuously.', 'OPEN'),
(1, 'Electrical', 'Living room ceiling fan regulator not functioning.', 'RESOLVED'),
(2, 'Carpentry', 'Main door lock gets jammed when closing.', 'OPEN'),
(3, 'Plumbing', 'Bathroom flush tank valve is broken.', 'IN_PROGRESS');