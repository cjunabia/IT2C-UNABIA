package unabia1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    placeOrder(scanner);
                    break;
                case 2:
                    viewHistory();
                    break;
                case 3:
                    updatePrices(scanner);
                    break;
                case 4:
                    System.out.println("Thank you for visiting the Coffee Shop!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
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

    private static void placeOrder(Scanner scanner) {
        System.out.println("\nAvailable Coffees:");
        for (int i = 0; i < coffeeDatabase.getCoffeeMenu().size(); i++) {
            Coffee coffee = coffeeDatabase.getCoffeeMenu().get(i);
            System.out.println((i + 1) + ". " + coffee.getName() + " - $" + coffee.getPrice());
        }

        System.out.print("Choose your coffee (1-" + coffeeDatabase.getCoffeeMenu().size() + "): ");
        int coffeeChoice = scanner.nextInt() - 1;

        String[] sizes = {"Small", "Medium", "Large"};
        double[] sizePrices = {0.00, 0.50, 1.00};

        System.out.println("\nAvailable Sizes:");
        for (int i = 0; i < sizes.length; i++) {
            System.out.println((i + 1) + ". " + sizes[i] + " - $" + sizePrices[i]);
        }
        System.out.print("Choose size (1-3): ");
        int sizeChoice = scanner.nextInt() - 1;

        String[] addOns = {"Milk", "Sugar", "Vanilla", "Caramel"};
        double[] addOnPrices = {0.50, 0.30, 0.70, 0.80};

        double addOnTotal = 0;
        System.out.println("\nAvailable Add-ons (Choose 0 to skip):");
        for (int i = 0; i < addOns.length; i++) {
            System.out.println((i + 1) + ". " + addOns[i] + " - $" + addOnPrices[i]);
        }
        System.out.print("Choose add-ons (comma-separated, e.g., 1,3 for Milk and Vanilla): ");
        scanner.nextLine(); // Consume newline
        String addOnInput = scanner.nextLine();
        String[] selectedAddOns = addOnInput.split(",");

        for (String addOn : selectedAddOns) {
            if (!addOn.trim().equals("0")) {
                int addOnIndex = Integer.parseInt(addOn.trim()) - 1;
                if (addOnIndex >= 0 && addOnIndex < addOns.length) {
                    addOnTotal += addOnPrices[addOnIndex];
                }
            }
        }

        Coffee selectedCoffee = coffeeDatabase.getCoffeeMenu().get(coffeeChoice);
        double totalPrice = selectedCoffee.getPrice() + sizePrices[sizeChoice] + addOnTotal;

        Order newOrder = new Order(selectedCoffee.getName(), sizes[sizeChoice], addOnInput, totalPrice);
        coffeeDatabase.addOrder(newOrder);

        System.out.println("Order placed successfully!");
        System.out.println("Coffee: " + selectedCoffee.getName());
        System.out.println("Size: " + sizes[sizeChoice]);
        System.out.println("Add-ons: " + addOnInput);
        System.out.println("Total Price: $" + totalPrice);

        saveOrderToDatabase(newOrder);
    }

   private static void saveOrderToDatabase(Order order) {
    String insertQuery = "INSERT INTO purchase_history (coffee_type, size, add_ons, total_price) VALUES (?, ?, ?, ?)";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

        preparedStatement.setString(1, order.getCoffeeType());
        preparedStatement.setString(2, order.getSize());
        preparedStatement.setString(3, order.getAddOns());
        preparedStatement.setDouble(4, order.getTotalPrice());

        // No need to set the 'date' field explicitly, SQLite handles it with CURRENT_TIMESTAMP
        preparedStatement.executeUpdate();
        System.out.println("Order saved to database.");
    } catch (SQLException e) {
        System.out.println("Error saving order to database: " + e.getMessage());
    }
}


   private static void viewHistory() {
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM purchase_history");
         ResultSet resultSet = preparedStatement.executeQuery()) {

        if (!resultSet.next()) {
            System.out.println("\nNo previous orders found.");
        } else {
            System.out.println("\nPurchase History:");
            do {
                String coffeeType = resultSet.getString("coffee_type");
                String size = resultSet.getString("size");
                String addOns = resultSet.getString("add_ons");
                double totalPrice = resultSet.getDouble("total_price");

                // Handling the date column as a string and parsing it manually
                String timestampString = resultSet.getString("date");
                Timestamp timestamp = null;

                // Check if the timestamp is not null or empty
                if (timestampString != null && !timestampString.isEmpty()) {
                    timestamp = Timestamp.valueOf(timestampString); // Convert string to Timestamp
                }

                System.out.println("Coffee: " + coffeeType);
                System.out.println("Size: " + size);
                System.out.println("Add-ons: " + addOns);
                System.out.println("Total Price: $" + totalPrice);
                System.out.println("Order Date: " + (timestamp != null ? timestamp : "N/A"));
                System.out.println("-------------------------");
            } while (resultSet.next());
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving purchase history: " + e.getMessage());
    }
}
  

    private static void updatePrices(Scanner scanner) {
        System.out.println("\nCurrent Coffee Prices:");
        for (int i = 0; i < coffeeDatabase.getCoffeeMenu().size(); i++) {
            Coffee coffee = coffeeDatabase.getCoffeeMenu().get(i);
            System.out.println((i + 1) + ". " + coffee.getName() + " - $" + coffee.getPrice());
        }

        System.out.print("Choose the coffee to update the price (1-" + coffeeDatabase.getCoffeeMenu().size() + "): ");
        int coffeeChoice = scanner.nextInt() - 1;
        System.out.print("Enter the new price for " + coffeeDatabase.getCoffeeMenu().get(coffeeChoice).getName() + ": $");
        double newPrice = scanner.nextDouble();

        coffeeDatabase.getCoffeeMenu().get(coffeeChoice).setPrice(newPrice);
        System.out.println(coffeeDatabase.getCoffeeMenu().get(coffeeChoice).getName() + " price updated to $" + newPrice);
    }

    static class Coffee {
        private String name;
        private double price;

        public Coffee(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    static class Order {
        private String coffeeType;
        private String size;
        private String addOns;
        private double totalPrice;

        public Order(String coffeeType, String size, String addOns, double totalPrice) {
            this.coffeeType = coffeeType;
            this.size = size;
            this.addOns = addOns;
            this.totalPrice = totalPrice;
        }

        public String getCoffeeType() {
            return coffeeType;
        }

        public String getSize() {
            return size;
        }

        public String getAddOns() {
            return addOns;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }

    static class CoffeeDatabase {
        private List<Coffee> coffeeMenu = new ArrayList<>();
        private List<Order> purchaseHistory = new ArrayList<>();

        public void addCoffee(String name, double price) {
            coffeeMenu.add(new Coffee(name, price));
        }

        public void addOrder(Order order) {
            purchaseHistory.add(order);
        }

        public List<Coffee> getCoffeeMenu() {
            return coffeeMenu;
        }

        public List<Order> getPurchaseHistory() {
            return purchaseHistory;
        }
    }

    static class DatabaseConnection {
        private static final String URL = "jdbc:sqlite:coffee_shop.db";  // Path to SQLite database

        // Method to establish a connection to the database
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL);
        }

        // Method to initialize the database schema if it doesn't already exist
        public static void initializeDatabase() {
            String createTableSQL = 
                "CREATE TABLE IF NOT EXISTS purchase_history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "coffee_type TEXT NOT NULL, " +
                "size TEXT NOT NULL, " +
                "add_ons TEXT, " +
                "total_price REAL NOT NULL, " +
                "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSQL);
                System.out.println("Database initialized successfully.");
            } catch (SQLException e) {
                System.out.println("Error initializing database: " + e.getMessage());
            }
        }
    }
    public static void clearDatabase() {
    String deleteQuery = "DELETE FROM purchase_history";
    try (Connection connection = DatabaseConnection.getConnection();
         Statement statement = connection.createStatement()) {
        statement.executeUpdate(deleteQuery);
        System.out.println("Database cleared.");
    } catch (SQLException e) {
        System.out.println("Error clearing the database: " + e.getMessage());
    }
}
public static void resetDatabase() {
    String dropQuery = "DROP TABLE IF EXISTS purchase_history";
    String createQuery = 
        "CREATE TABLE IF NOT EXISTS purchase_history (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "coffee_type TEXT NOT NULL, " +
        "size TEXT NOT NULL, " +
        "add_ons TEXT, " +
        "total_price REAL NOT NULL, " +
        "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");";

    try (Connection connection = DatabaseConnection.getConnection();
         Statement statement = connection.createStatement()) {
        // Drop the table first
        statement.executeUpdate(dropQuery);
        // Recreate the table
        statement.executeUpdate(createQuery);
        System.out.println("Database reset.");
    } catch (SQLException e) {
        System.out.println("Error resetting the database: " + e.getMessage());
    }
}

}
