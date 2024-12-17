# Car Park Availability System

This project is a **Car Park Management System** that processes car park data and updates the availability of parking lots. It includes batch processing, scheduled updates, and data storage functionalities.

---

## **Features**

- Load car park data from CSV files and store it in a database.
- Scheduled updates to fetch real-time car park availability.
- Coordinate conversion from **SVY21** to **WGS84** (for latitude and longitude).
- Handles large datasets efficiently using Spring Boot and batch processing.
- Comprehensive exception handling and logging for data consistency.

---

## **Tech Stack**

- **Backend**: Java 17, Spring Boot 3.x
- **Database**: MySQL (configurable)
- **Libraries**:
  - Jackson (JSON processing)
  - Apache Commons CSV (CSV parsing)
  - Spring Data JPA (Database integration)
  - Lombok (Simplified boilerplate code)
- **Build Tool**: Maven
- **Scheduling**: Spring Task Scheduler

---

## **Getting Started**

### Prerequisites

Ensure you have the following installed on your machine:
- Java 17 or higher
- Maven 3.8+
- MySQL database
- Git

---

### **Setup Instructions**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/gattemvijay/CarPark.git
   cd CarPark
