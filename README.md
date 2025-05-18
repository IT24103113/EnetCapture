# 📸 Event Photography and Videography Booking System

## Overview

The **Event Photography and Videography Booking System** is a robust web-based platform developed to efficiently connect clients with talented photographers and videographers. The system simplifies every step of the process—from finding professionals, booking services, managing event details, to gathering post-event feedback—using solid Object-Oriented Programming (OOP) principles and file-based data storage.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [OOP Concepts by Component](#oop-concepts-by-component)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [License](#license)

---

## Features

### For Clients
- Register, log in, and manage personal profiles
- Search and browse for professionals by service, rating, and availability
- Book photographers/videographers and monitor booking statuses
- View booking history and event information
- Submit reviews and ratings after events

### For Professionals
- Register and manage professional profiles and service details
- Configure service types, pricing, and availability
- Receive and respond to booking requests
- View event schedules and client feedback

### For Administrators
- Access a centralized dashboard for system monitoring and management
- Oversee and manage user and professional accounts
- Approve or verify professional profiles
- Configure global system settings

---

## System Architecture

The application is organized into six primary components, each leveraging OOP principles and the MVC (Model-View-Controller) design pattern:

1. **User Management**
    - Manages registration, authentication, and profile management with role-based access.
    - Data stored in `users.txt`.

2. **Professional Management**
    - Enables professionals to manage profiles and configure services.
    - Supports sorting professionals by ratings.
    - Data stored in `photographers.txt`.

3. **Booking Management**
    - Implements queue-based booking for first-come, first-served logic.
    - Tracks booking status and lifecycle.
    - Data stored in `bookings.txt`.

4. **Admin Management**
    - Provides administrative controls and system operations.
    - Data stored in `admins.txt`.

5. **Feedback & Review**
    - Handles client reviews and professional rating calculations.
    - Data stored in `reviews.txt`.

6. **Event Management**
    - Supports event creation, configuration, and status management.

---

## OOP Concepts by Component

| Component               | OOP Concepts Applied                                                                                                                                |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| **User Management**         | **Encapsulation:** User attributes are private, accessed via getters/setters.<br>**Inheritance:** Base `User` class with `Client`, `Professional`, and `Admin` subclasses.<br>**Polymorphism:** Overridden login/registration logic for different user types.<br>**Abstraction:** Abstract methods for shared behaviors. |
| **Professional Management** | **Inheritance & Polymorphism:** `Professional` extends `User`, overrides availability and service listing.<br>**Encapsulation:** Profile data is private.<br>**Polymorphism:** Methods for displaying or updating professional info are overridden. |
| **Booking Management**      | **Encapsulation:** Booking details are private, accessed via methods.<br>**Polymorphism:** Handles bookings for different user roles.<br>**Abstraction:** Abstract booking handling for extensibility.<br>**MVC:** Controllers manage booking logic, models represent booking data. |
| **Admin Management**        | **Encapsulation:** Admin data is private.<br>**Inheritance:** `Admin` is a subclass of `User`.<br>**Singleton Pattern:** Ensures a single admin control instance for system configuration.<br>**MVC:** Admin controllers and views for system management. |
| **Feedback & Review**       | **Encapsulation:** Review content and ratings are private.<br>**Strategy Pattern:** Different algorithms for calculating ratings can be swapped.<br>**Polymorphism:** Different review processing for clients and professionals.<br>**MVC:** Models for reviews, controllers for processing. |
| **Event Management**        | **Builder Pattern:** Flexible event creation.<br>**Encapsulation:** Event details are private.<br>**State Pattern:** Manages event states (upcoming, ongoing, completed).<br>**MVC:** Controllers handle event logic, models represent events. |

> **Note:** The MVC (Model-View-Controller) design pattern is consistently applied throughout the application to maintain separation of concerns and facilitate organized, maintainable code.

---

## Technologies Used

- **Backend:** Java (Servlets, JSP)
- **Frontend:** HTML, CSS, JavaScript, Bootstrap or Tailwind CSS
- **Data Storage:** File-based (`.txt` files)
- **IDE:** IntelliJ IDEA
- **Version Control:** Git (GitHub, feature branching workflow)

---

## Project Structure

The project is modularized for maintainability and scalability:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── enetcapture/
│   │           ├── controller/     # Handles HTTP requests and routes
│   │           ├── model/          # Domain models (User, Booking, Event, etc.)
│   │           └── service/        # Business logic and processing
│   ├── resources/                  # Configuration files (if needed)
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── views/              # JSP files for UI rendering
│       │   └── web.xml             # Deployment descriptor
│       └── index.jsp               # Main landing page
├── test/                           # Unit and integration tests
├── .gitignore                      # Git ignored files
├── mvnw                            # Maven wrapper script
├── mvnw.cmd                        # Maven wrapper for Windows
└── pom.xml                         # Maven project configuration
```

---

## License

This project is licensed under the [MIT License](LICENSE).