# Food Ordering System

## Overview
This is a food ordering system that allows users to browse menus, place orders, and manage their food orders efficiently. The backend is built using **Kotlin** with a focus on scalability, performance, and clean architecture.

## Tech Stack
- **Backend**: Kotlin, Spring Boot
- **Database**: MySQL (or any preferred database)
- **API**: RESTful APIs
- **Build Tool**: Gradle
- **Other Tools**: Docker

## Prerequisites
Before setting up the project, ensure you have the following installed:
- JDK 17 or later
- Gradle 7.x or later
- MySQL (or your preferred database)
- Docker (optional, for containerization)
- IDE (IntelliJ IDEA recommended)

## Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/PawPasta/SWD392_Fa25_Chiicken_Kitchen_BE.git
   ```

2. **Set up the database**:
    - Create a MySQL database:
      ```sql
      CREATE DATABASE IF NOT EXISTS chicken_kitchen_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
      ```
    - Update the database configuration in `src/main/resources/application.properties`:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/kitchen_chicken_db
      spring.datasource.username=root
      spring.datasource.password=1234
      spring.jpa.hibernate.ddl-auto=update
      ```

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

4. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

5. **Access the API**:
    - The application will be available at `http://localhost:8080`.
    - Use tools like Postman or cURL to test the REST APIs.

## Project Structure
```
food-ordering-system/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   ├── controller/    # REST controllers
│   │   │   ├── service/       # Business logic
│   │   │   ├── repository/    # Data access layer
│   │   │   ├── model/         # Data models/entities
│   │   └── resources/
│   │       ├── application.properties  # Configuration file
├── build.gradle            # Gradle build file
├── README.md               # Project documentation
└── docker-compose.yml      # Optional Docker configuration
```

## API Endpoints
Pending ... 
