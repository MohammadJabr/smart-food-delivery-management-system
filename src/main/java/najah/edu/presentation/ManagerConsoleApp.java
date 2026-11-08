
package najah.edu.presentation;

import java.util.List;
import java.util.Scanner;

import najah.edu.domain.Driver;
import najah.edu.domain.Order;
import najah.edu.service.DriverAssignmentService;
import najah.edu.service.OrderService;
import najah.edu.strategy.FastestDriverStrategy;
import najah.edu.strategy.NearestDriverStrategy;
import najah.edu.service.PaymentService;
public class ManagerConsoleApp {

	public static void run(
	        Scanner scanner,
	        OrderService orderService,
	        List<Driver> drivers,
	        PaymentService paymentService)  {

        boolean running = true;

        while (running) {

            System.out.println("\n========== MANAGER MENU ==========");
            System.out.println("1. View All Orders");
            System.out.println("2. Update Order Status");
            System.out.println("3. Assign Driver");
            System.out.println("0. Logout");

            int choice = readInt(scanner, "Choose: ", 0, 3);

            switch (choice) {

            case 1:
                viewOrders(orderService, paymentService);
                break;

            case 2:
                updateStatus(scanner, orderService, paymentService);
                break;

            case 3:
                assignDriver(
                        scanner, orderService, drivers, paymentService);
                break;

                case 0:
                    running = false;
                    System.out.println("Manager logged out.");
                    break;
            }
        }
    }

	private static void viewOrders(
	        OrderService service,
	        PaymentService paymentService) { {

        List<Order> orders = service.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\n========== ALL ORDERS ==========");

        for (int i = 0; i < orders.size(); i++) {

            Order order = orders.get(i);

            System.out.printf(
                    "%d. %s | %s | %.2f | %s | Payment: %s%n",
                    i + 1,
                    order.getRestaurant().getName(),
                    order.getOrderType(),
                    order.getFinalBill(),
                    order.getStatus(),
                    paymentService.getPaymentStatus(order)
            );
        }
	        }
    }

	private static void updateStatus(
	        Scanner scanner,
	        OrderService service,
	        PaymentService paymentService)  {

		Order order = selectOrder(
		        scanner, service, paymentService);

        if (order == null) {
            return;
        }

        System.out.println("\nCurrent Status: " + order.getStatus());
        System.out.println("1. Confirm Order");
        System.out.println("2. Start Preparing");
        System.out.println("3. Mark Ready");
        System.out.println("4. Out for Delivery");
        System.out.println("5. Mark Delivered");

        int choice = readInt(scanner, "Choose action: ", 1, 5);

        try {

            switch (choice) {

                case 1:
                    service.confirmOrder(order);
                    break;

                case 2:
                    service.startPreparing(order);
                    break;

                case 3:
                    service.markReady(order);
                    break;

                case 4:
                    service.markOutForDelivery(order);
                    break;

                case 5:
                    service.markDelivered(order);
                    break;
            }

            System.out.println(
                    "Order status updated to: " + order.getStatus());

        } catch (IllegalStateException e) {

            System.out.println(
                    "Cannot update order: " + e.getMessage());
        }
    }

	private static void assignDriver(
	        Scanner scanner,
	        OrderService service,
	        List<Driver> drivers,
	        PaymentService paymentService)  {

		Order order = selectOrder(
		        scanner, service, paymentService);

        if (order == null) {
            return;
        }

        if (order.getAssignedDriver() != null) {
            System.out.println("A driver is already assigned.");
            return;
        }

        System.out.println("\nChoose Driver Selection Strategy:");
        System.out.println("1. Nearest Driver");
        System.out.println("2. Fastest Driver");

        int choice = readInt(scanner, "Choose: ", 1, 2);

        DriverAssignmentService assignmentService;

        if (choice == 1) {
            assignmentService =
                    new DriverAssignmentService(
                            new NearestDriverStrategy());
        } else {
            assignmentService =
                    new DriverAssignmentService(
                            new FastestDriverStrategy());
        }

        try {

            Driver driver =
                    assignmentService.assignDriver(order, drivers);

            System.out.println(
                    "Driver assigned successfully: "
                    + driver.getName());

            System.out.println(
                    "Driver Type: " + driver.getDriverType());

        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            System.out.println(
                    "Driver assignment failed: " + e.getMessage());
        }
    }

	private static Order selectOrder(
	        Scanner scanner,
	        OrderService service,
	        PaymentService paymentService)   {

        List<Order> orders = service.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return null;
        }

        viewOrders(service, paymentService);

        int choice = readInt(
                scanner,
                "Select order number (0 to go back): ",
                0,
                orders.size());

        if (choice == 0) {
            return null;
        }

        return orders.get(choice - 1);
    }

    private static int readInt(
            Scanner scanner,
            String message,
            int min,
            int max) {

        while (true) {

            System.out.print(message);

            try {

                int value = Integer.parseInt(
                        scanner.nextLine().trim());

                if (value >= min && value <= max) {
                    return value;
                }

            } catch (NumberFormatException e) {
                // Ask again.
            }

            System.out.println("Invalid selection.");
        }
    }
}
