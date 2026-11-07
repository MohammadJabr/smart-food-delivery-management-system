
package najah.edu.presentation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import najah.edu.domain.MenuItem;
import najah.edu.domain.Order;
import najah.edu.domain.OrderItem;
import najah.edu.domain.OrderType;
import najah.edu.domain.Restaurant;
import najah.edu.domain.User;

import najah.edu.repository.MenuRepository;
import najah.edu.repository.OrderRepository;
import najah.edu.repository.RestaurantRepository;
import najah.edu.repository.UserRepository;

import najah.edu.service.LoginService;
import najah.edu.service.MenuService;
import najah.edu.service.OrderService;
import najah.edu.service.RestaurantService;

import najah.edu.domain.Driver;
import java.util.Arrays;
import najah.edu.service.PaymentService;

import najah.edu.domain.BicycleDriver;
import najah.edu.domain.MotorcycleDriver;
import najah.edu.domain.CarDriver;
import najah.edu.domain.ElectricVehicleDriver;

import najah.edu.service.PricingService;

import najah.edu.strategy.NormalPricingStrategy;
import najah.edu.strategy.PeakHourPricingStrategy;
import najah.edu.strategy.PromotionalPricingStrategy;
import najah.edu.strategy.ExpressDeliveryPricingStrategy;
import najah.edu.notification.EmailNotificationService;

public class MenuConsoleApp {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


 public static void main(String[] args) {

    try (Scanner scanner = new Scanner(System.in)) {

        UserRepository userRepository =
                new UserRepository("users.txt");

        LoginService loginService =
                new LoginService(userRepository.getUsers());

        ArrayList<Restaurant> restaurants;

        try {
            RestaurantRepository restaurantRepository =
                    new RestaurantRepository("restaurants.txt");

            restaurants = restaurantRepository.getRestaurants();

            MenuRepository menuRepository =
                    new MenuRepository("menu.txt");

            menuRepository.loadMenuItems(restaurants);

        } catch (IllegalStateException |
                 IllegalArgumentException e) {

            System.err.println(
                    "Could not load restaurant data: " + e.getMessage());
            return;
        }

        OrderRepository orderRepository = new OrderRepository();

        OrderService orderService =
                new OrderService(orderRepository);

        PaymentService paymentService = new PaymentService();

        List<Driver> drivers = Arrays.asList(

                new BicycleDriver(
                        "Ahmad",
                        Driver.Status.AVAILABLE,
                        2.5,
                        15
                ),

                new MotorcycleDriver(
                        "Omar",
                        Driver.Status.AVAILABLE,
                        4.0,
                        10
                ),

                new CarDriver(
                        "Sara",
                        Driver.Status.AVAILABLE,
                        1.5,
                        20
                ),

                new ElectricVehicleDriver(
                        "Khaled",
                        Driver.Status.AVAILABLE,
                        3.0,
                        12,
                        25.0
                )
        );

        while (true) {

            User loggedInUser = login(scanner, loginService);

            if (loggedInUser == null) {
                return;
            }

            System.out.println("\nLogin successful!");
            System.out.println("Welcome, " + loggedInUser.getName());

            if ("CUSTOMER".equalsIgnoreCase(loggedInUser.getRole())) {

            	customerMenu(
            	        scanner,
            	        restaurants,
            	        orderService,
            	        paymentService,
            	        loggedInUser
            	);

            } else if ("MANAGER".equalsIgnoreCase(loggedInUser.getRole())) {

            	ManagerConsoleApp.run(
            	        scanner,
            	        orderService,
            	        drivers,
            	        paymentService
            	);

            } else {

                System.out.println("Unknown user role.");
            }

            System.out.println("\nReturning to login screen...");
        }
    }
}


    private static User login(
            Scanner scanner,
            LoginService loginService) {

        System.out.println("================================");
        System.out.println("   SMART FOOD DELIVERY SYSTEM");
        System.out.println("================================");

        for (int attempt = 1; attempt <= 3; attempt++) {

            System.out.print("\nUsername: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            try {
                return loginService.login(username, password);

            } catch (SecurityException |
                     IllegalArgumentException e) {

                System.out.println("Invalid username or password.");
                System.out.println(
                        "Attempts remaining: " + (3 - attempt));
            }
        }

        System.out.println("\nMaximum login attempts exceeded.");

        return null;
    }

    private static void customerMenu(
            Scanner scanner,
            ArrayList<Restaurant> restaurants,
            OrderService orderService,
            PaymentService paymentService,
            User currentCustomer)  {

        List<Order> myOrders = new ArrayList<>();

        boolean running = true;

        while (running) {

            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. Browse Restaurants and Place Order");
            System.out.println("2. View My Orders");
            System.out.println("3. Cancel an Order");
            System.out.println("4. Pay for an Order");
            System.out.println("0. Logout");

            int choice = readInt(scanner, "Choose an option: ", 0, 4);

            switch (choice) {

            case 1:
            	Order order = placeOrder(
            	        scanner,
            	        restaurants,
            	        orderService,
            	        currentCustomer
            	);

                if (order != null) {
                    myOrders.add(order);
                }
                break;

            case 2:
                viewOrders(myOrders, paymentService);
                break;

            case 3:
                cancelOrder(
                        scanner, myOrders, orderService, paymentService);
                break;

            case 4:
                payForOrder(scanner, myOrders, paymentService);
                break;

            case 0:
                running = false;
                System.out.println("Logged out successfully.");
                break;
            }
        }
    }

    private static Order placeOrder(
            Scanner scanner,
            ArrayList<Restaurant> restaurants,
            OrderService orderService,
            User currentCustomer) {

        RestaurantService restaurantService =
                new RestaurantService(restaurants);

        List<Restaurant> availableRestaurants =
                restaurantService.getAvailableRestaurants();

        if (availableRestaurants.isEmpty()) {
            System.out.println("No open restaurants available.");
            return null;
        }

        System.out.println("\nAvailable Restaurants:");

        for (int i = 0; i < availableRestaurants.size(); i++) {
            System.out.println(
                    (i + 1) + ". "
                    + availableRestaurants.get(i).getName());
        }

        int restaurantChoice = readInt(
                scanner,
                "Choose a restaurant (0 to go back): ",
                0,
                availableRestaurants.size()
        );

        if (restaurantChoice == 0) {
            return null;
        }

        Restaurant selectedRestaurant =
                availableRestaurants.get(restaurantChoice - 1);

        MenuService menuService = new MenuService();

        List<MenuItem> availableItems =
                menuService.getAvailableMenuItems(selectedRestaurant);

        if (availableItems.isEmpty()) {
            System.out.println("No available menu items.");
            return null;
        }

        System.out.println(
                "\nMenu for " + selectedRestaurant.getName());

        for (int i = 0; i < availableItems.size(); i++) {

            MenuItem item = availableItems.get(i);

            System.out.printf(
                    "%d. %s - %.2f%n",
                    i + 1,
                    item.getName(),
                    item.getPrice()
            );
        }

        System.out.println("\nChoose items one by one.");
        System.out.println("Enter 0 when finished.");

        int typeChoice = readInt(
                scanner,
                "\nOrder Type (1 Standard, 2 Express, 3 Scheduled): ",
                1,
                3
        );

        OrderType orderType;

        switch (typeChoice) {
            case 1:
                orderType = OrderType.STANDARD;
                break;
            case 2:
                orderType = OrderType.EXPRESS;
                break;
            default:
                orderType = OrderType.SCHEDULED;
                break;
        }

        Order order = orderService.createOrder(
                selectedRestaurant, orderType);

        int selectedCount = 0;

        while (true) {

            int itemChoice = readInt(
                    scanner,
                    "\nChoose item number (0 to finish): ",
                    0,
                    availableItems.size()
            );

            if (itemChoice == 0) {
                break;
            }

            int quantity = readInt(
                    scanner,
                    "Enter quantity: ",
                    1,
                    99
            );

            try {

                MenuItem selectedItem =
                        availableItems.get(itemChoice - 1);

                orderService.addItem(
                        order, selectedItem, quantity);

                selectedCount++;

                System.out.println("Item added successfully.");

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        if (selectedCount == 0) {
            System.out.println("No items selected. Order cancelled.");
            return null;
        }

        System.out.print("\nDelivery Address: ");
        String address = scanner.nextLine().trim();

        double distance = readDouble(
                scanner,
                "Delivery Distance in KM: "
        );

        try {

            orderService.setDeliveryDetails(
                    order, address, distance);

            if (orderType == OrderType.SCHEDULED) {

                LocalDateTime deliveryTime =
                        readScheduledTime(scanner);

                orderService.setScheduledDeliveryTime(
                        order, deliveryTime);
            }

            PricingService pricingService =
                    choosePricingService(scanner, orderType);

            double finalPrice =
                    pricingService.calculateFinalPrice(order);

            order.setPricedFinalBill(finalPrice);

            configureOrderNotifications(
                    scanner,
                    currentCustomer,
                    order
            );

            orderService.placeOrder(order);

            System.out.println("\nOrder placed successfully!");

            displayReceipt(order, orderService);

            return order;

        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            System.out.println(
                    "\nCould not place order: " + e.getMessage());

            return null;
        }
    }

    private static LocalDateTime readScheduledTime(
            Scanner scanner) {

        while (true) {

            System.out.print(
                    "Scheduled Delivery (yyyy-MM-dd HH:mm): ");

            String input = scanner.nextLine().trim();

            try {
                return LocalDateTime.parse(input, DATE_FORMAT);

            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date format. Example: 2026-11-08 18:30");
            }
        }
    }

    private static void displayReceipt(
            Order order,
            OrderService orderService) {

        System.out.println("\n========== ORDER RECEIPT ==========");

        System.out.println(
                "Restaurant: " + order.getRestaurant().getName());

        System.out.println(
                "Order Type: " + order.getOrderType());

        System.out.println("\nItems:");

        for (OrderItem item : order.getItems()) {

            System.out.printf(
                    "%s x%d = %.2f%n",
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    item.getSubtotal()
            );
        }

        System.out.printf(
                "\nFood Total: %.2f%n",
                order.getTotal());

        System.out.printf(
                "Delivery Fee: %.2f%n",
                order.getDeliveryFee());

        System.out.printf(
                "Final Bill: %.2f%n",
                order.getFinalBill());

        System.out.println(
                "Delivery Address: " + order.getDeliveryAddress());

        System.out.println(
                "Status: " + order.getStatus());

        if (order instanceof najah.edu.domain.ScheduledOrder) {

            najah.edu.domain.ScheduledOrder scheduledOrder =
                    (najah.edu.domain.ScheduledOrder) order;

            System.out.println(
                    "Scheduled Time: "
                    + scheduledOrder.getScheduledDeliveryTime());
        }

        System.out.println("===================================");
        System.out.println(
                "Your Order Number: "
                + orderService.getAllOrders().size());
    }

    private static void viewOrders(
            List<Order> orders,
            PaymentService paymentService) {

        if (orders.isEmpty()) {
            System.out.println("You have no orders yet.");
            return;
        }

        System.out.println("\n========== MY ORDERS ==========");

        for (int i = 0; i < orders.size(); i++) {

            Order order = orders.get(i);

            System.out.printf(
                    "%d. %s | %s | %.2f | %s%n",
                    i + 1,
                    order.getRestaurant().getName(),
                    order.getOrderType(),
                    order.getFinalBill(),
                    order.getStatus()
            );
        }
    }


private static void configureOrderNotifications(
        Scanner scanner,
        User customer,
        Order order) {

    String recipientEmail = customer.getEmail();

    if (recipientEmail == null || recipientEmail.trim().isEmpty()) {

        System.out.print(
                "Enter email for order notifications: ");

        recipientEmail = scanner.nextLine().trim();
    }

    if (recipientEmail.isEmpty()) {
        System.out.println("Email notifications disabled.");
        return;
    }

    try {

    	EmailNotificationService notificationService =
    	        new EmailNotificationService(recipientEmail);

        order.addObserver(notificationService);

        System.out.println("Email notifications enabled.");

    } catch (IllegalArgumentException |
             IllegalStateException e) {

        System.out.println(
                "Could not enable email notifications: "
                + e.getMessage());
    }
}


    private static void cancelOrder(
            Scanner scanner,
            List<Order> orders,
            OrderService orderService,
            PaymentService paymentService) {

        if (orders.isEmpty()) {
            System.out.println("You have no orders to cancel.");
            return;
        }

        viewOrders(orders, paymentService);

        int choice = readInt(
                scanner,
                "Enter order number to cancel (0 to go back): ",
                0,
                orders.size()
        );

        if (choice == 0) {
            return;
        }

        try {

            orderService.cancelOrder(
                    orders.get(choice - 1));

            System.out.println("Order cancelled successfully.");

        } catch (IllegalStateException e) {

            System.out.println(
                    "Cannot cancel order: " + e.getMessage());
        }
    }

    private static int readInt(
            Scanner scanner,
            String message,
            int min,
            int max) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                }

            } catch (NumberFormatException e) {
                // Ask again.
            }

            System.out.println(
                    "Invalid input. Enter a number between "
                    + min + " and " + max + ".");
        }
    }

    private static double readDouble(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                double value = Double.parseDouble(input);

                if (Double.isFinite(value) && value > 0) {
                    return value;
                }

            } catch (NumberFormatException e) {
                // Ask again.
            }

            System.out.println(
                    "Please enter a valid positive number.");
        }
    }


private static PricingService choosePricingService(
        Scanner scanner,
        OrderType orderType) {

    System.out.println("\nChoose Pricing Strategy:");
    System.out.println("1. Normal Pricing");
    System.out.println("2. Peak Hour Pricing (+20%)");
    System.out.println("3. Promotional Pricing (-20%)");

    if (orderType == OrderType.STANDARD) {
        System.out.println("4. Express Delivery Pricing (+8.00)");
    }

    int maxChoice =
            orderType == OrderType.STANDARD ? 4 : 3;

    int choice = readInt(
            scanner,
            "Choose pricing strategy: ",
            1,
            maxChoice
    );

    switch (choice) {

        case 1:
            return new PricingService(
                    new NormalPricingStrategy());

        case 2:
            return new PricingService(
                    new PeakHourPricingStrategy(0.20));

        case 3:
            return new PricingService(
                    new PromotionalPricingStrategy(0.20));

        case 4:
            return new PricingService(
                    new ExpressDeliveryPricingStrategy(8.0));

        default:
            throw new IllegalArgumentException(
                    "Invalid pricing strategy");
    }
}


    private static void payForOrder(
            Scanner scanner,
            List<Order> orders,
            PaymentService paymentService) {

        if (orders.isEmpty()) {
            System.out.println("You have no orders.");
            return;
        }

        viewOrders(orders, paymentService);

        int choice = readInt(
                scanner,
                "Choose order number (0 to go back): ",
                0,
                orders.size()
        );

        if (choice == 0) {
            return;
        }

        Order order = orders.get(choice - 1);

        if (paymentService.isPaid(order)) {
            System.out.println("This order has already been paid.");
            return;
        }

        System.out.printf(
                "Amount due: %.2f%n",
                order.getFinalBill());

        double amount = readDouble(
                scanner,
                "Enter cash payment amount: ");

        try {

            paymentService.processCashPayment(order, amount);

            System.out.println("Payment successful!");
            System.out.println("Payment Status: PAID");

        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            System.out.println("Payment failed: " + e.getMessage());
        }
    }
}
