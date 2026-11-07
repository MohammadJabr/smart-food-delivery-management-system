# Smart Food Delivery Management System

A Java-based console application developed as a Software Engineering course project at An-Najah National University. The system manages customers, restaurants, food orders, payments, delivery drivers, and order notifications.

## Features

### User Management
- Customer and manager login.
- Role-based access to customer and manager operations.
- Customer email support for order notifications.

### Restaurant and Menu Management
- Browse available restaurants.
- View restaurant menus.
- Select food items and quantities.

### Order Management
- Create Standard, Express, and Scheduled orders.
- Add multiple items to an order.
- Validate order details and delivery information.
- View and cancel eligible orders.
- Track order status through its lifecycle:
  `PENDING → CONFIRMED → PREPARING → READY → OUT_FOR_DELIVERY → DELIVERED`

### Pricing and Payments
- Normal pricing.
- Peak-hour pricing.
- Promotional discounts.
- Express delivery fees.
- Cash payment processing.

### Driver Management
- Assign available drivers to orders.
- Choose drivers using Nearest or Fastest assignment strategies.
- Support Bicycle, Motorcycle, Car, and Electric Vehicle drivers.
- Apply vehicle-specific delivery restrictions.

### Notifications
- Notify customers about important order updates.
- Optional email notifications through Gmail SMTP.
- Email credentials are provided through environment variables.

## Technology Stack

- Java
- Maven
- JUnit 5
- Mockito
- JaCoCo
- Jakarta Mail
- Git and GitHub

## Project Architecture

The project is organized into four main layers:

| Layer | Responsibility |
|---|---|
| `domain` | Core entities, orders, drivers, and business models |
| `repository` | Data access and storage |
| `service` | Business logic and application operations |
| `presentation` | Console user interfaces |

## Design Patterns

The project uses the following design patterns:

- **Strategy Pattern:** Supports interchangeable pricing and driver-assignment strategies.
- **Observer Pattern:** Supports notifications when order events occur.

## Project Structure

```text
smart-food-delivery-management-system/
│
├── src/
│   ├── main/
│   │   └── java/najah/edu/
│   │       ├── domain/
│   │       ├── repository/
│   │       ├── service/
│   │       └── presentation/
│   │
│   └── test/
│       └── java/najah/edu/
│
├── restaurants.txt
├── menu.txt
├── users.txt
├── pom.xml
└── README.md
```

## Requirements

- JDK 11 or newer.
- Apache Maven.
- An IDE such as Eclipse or IntelliJ IDEA.

The Maven compiler is configured to target Java 8 compatibility.

## Running the Project

1. Clone the repository:

```bash
git clone https://github.com/MohammadJabr/smart-food-delivery-management-system.git
```

2. Open the project in your IDE as a Maven project.

3. Allow Maven to download the dependencies.

4. Run the following command from the project root to execute the tests:

```bash
mvn clean test
```

5. Run `najah.edu.presentation.MenuConsoleApp` as a Java Application from your IDE.

Run the application with the project root as its working directory so that the text data files can be located.

## Email Configuration

Email notifications require Gmail SMTP credentials.

Set the following environment variables on your machine:

```text
EMAIL_USERNAME=your-gmail-address
EMAIL_PASSWORD=your-gmail-app-password
```

Use a Gmail App Password rather than your regular Gmail password.

Do not commit actual email credentials or passwords to the repository.

Customers can provide an email address for receiving order notifications when placing an order.

## Testing and Code Coverage

Run all automated tests using:

```bash
mvn clean test
```

The project uses JUnit 5 and Mockito for automated testing.

JaCoCo generates the code coverage report at:

```text
target/site/jacoco/index.html
```

Maven test reports are available in:

```text
target/surefire-reports/
```

## Data Storage

Restaurant, menu, and user data are loaded from text files.

Order data is currently maintained in memory during application execution and is not retained after the application exits.

## Academic Project

Developed as part of the Software Engineering course at An-Najah National University.