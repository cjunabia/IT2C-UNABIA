package unabia1;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Unabia1 {

    static CoffeeDatabase coffeeDatabase = new CoffeeDatabase();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Initialize database and coffee menu
        DatabaseConnection.initializeDatabase();
        initializeCoffeeDatabase();

        while (running) {
            System.out.println("\n===== Welcome to the Coffee Shop! =====");
            System.out.println("1. Place a New Order");
            System.out.println("2. View Purchase History");
            System.out.println("3. Update Coffee Prices");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = -1;
            while (choice < 1 || choice > 4) {
                try {
                    choice = scanner.nextInt();
                    if (choice < 1 || choice > 4) {
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // clear the buffer
                }
            }

            switch (choice) {
                case 1:
                    OrderManager orderManager = new OrderManager();  // Create an instance of OrderManager
                    orderManager.placeOrder(scanner);  // Call the correct method signature
                    break;
                case 2:
                    HistoryManager.viewHistory();
                    break;
                case 3:
                    PriceManager.updatePrices(coffeeDatabase, scanner);
                    break;
                case 4:
                    System.out.println("Thank you for visiting the Coffee Shop!");
                    running = false;
                    break;
            }
        }

        scanner.close();
    }

    private static void initializeCoffeeDatabase() {
        coffeeDatabase.addCoffee("Espresso", 2.50);
        coffeeDatabase.addCoffee("Americano", 3.00);
        coffeeDatabase.addCoffee("Latte", 3.50);
        coffeeDatabase.addCoffee("Cappuccino", 3.75);
        coffeeDatabase.addCoffee("Mocha", 4.00);
    }

    static class Order {

        public Order() {
        }

        String getOrderManager() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        String getCoffeeType() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        String getSize() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        String getAddOns() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        double getTotalPrice() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    static class Coffee {

        public Coffee() {
        }
    }
}
