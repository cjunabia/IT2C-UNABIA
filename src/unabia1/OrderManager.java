package unabia1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import static unabia1.Unabia1.coffeeDatabase;

public class OrderManager {

    private static Object Unabia1;
    
public static Connection getConnection() throws SQLException {
    String url = "jdbc:sqlite:your_database_path_here.db";  // Adjust path if necessary
    return OrderManager.getConnection(url);
}

    private static Connection getConnection(String url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
   
   public void placeOrder(Scanner scanner) {
    System.out.println("\nAvailable Coffees:");
    List<Coffee> menu = coffeeDatabase.getCoffeeMenu();
    for (int i = 0; i < menu.size(); i++) {
        Coffee coffee = menu.get(i);
        System.out.println((i + 1) + ". " + coffee.getName() + " - $" + coffee.getPrice());
    }

    // Coffee selection
    int coffeeChoice = -1;
    while (coffeeChoice < 1 || coffeeChoice > menu.size()) {
        System.out.print("Choose your coffee (1-" + menu.size() + "): ");
        try {
            coffeeChoice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    Coffee selectedCoffee = menu.get(coffeeChoice - 1);

    // Size selection
    String[] sizes = {"Small", "Medium", "Large"};
    double[] sizePrices = {0.00, 0.50, 1.00};
    int sizeChoice = -1;

    while (sizeChoice < 1 || sizeChoice > sizes.length) {
        System.out.println("\nAvailable Sizes:");
        for (int i = 0; i < sizes.length; i++) {
            System.out.println((i + 1) + ". " + sizes[i] + " - $" + sizePrices[i]);
        }
        System.out.print("Choose size (1-3): ");
        try {
            sizeChoice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
        }
    }

    String size = sizes[sizeChoice - 1];
    double sizePrice = sizePrices[sizeChoice - 1];

    // Add-ons selection
    String[] addOns = {"Milk", "Sugar", "Vanilla", "Caramel"};
    double[] addOnPrices = {0.50, 0.30, 0.70, 0.80};
    double addOnTotal = 0;

    System.out.println("\nAvailable Add-ons (Choose 0 to skip):");
    for (int i = 0; i < addOns.length; i++) {
        System.out.println((i + 1) + ". " + addOns[i] + " - $" + addOnPrices[i]);
    }
    scanner.nextLine(); // Consume newline
    System.out.print("Choose add-ons (comma-separated, e.g., 1,3 for Milk and Vanilla): ");
    String addOnInput = scanner.nextLine();
    String[] selectedAddOns = addOnInput.split(",");

    for (String addOn : selectedAddOns) {
        try {
            int addOnIndex = Integer.parseInt(addOn.trim()) - 1;
            if (addOnIndex >= 0 && addOnIndex < addOns.length) {
                addOnTotal += addOnPrices[addOnIndex];
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + addOn + " is not a valid number.");
        }
    }

    // Calculate total price
    double totalPrice = selectedCoffee.getPrice() + sizePrice + addOnTotal;

    // Create and save the order
    Order newOrder = new Order(selectedCoffee.getName(), size, addOnInput, totalPrice);
    coffeeDatabase.addOrder(newOrder);  // Ensure this method also saves to the database

    // Save the order to the database
    saveOrderToDatabase(newOrder);

    System.out.println("\nOrder placed successfully!");
    System.out.println("-------- Receipt --------");
    System.out.println("Coffee: " + selectedCoffee.getName());
    System.out.println("Size: " + size);
    System.out.println("Add-ons: " + addOnInput);
    System.out.println("Total Price: $" + totalPrice);
}

private static void saveOrderToDatabase(Order order) {
    String insertQuery = "INSERT INTO purchase_history (coffee_type, size, add_ons, total_price) VALUES (?, ?, ?, ?)";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

        preparedStatement.setString(1, order.getCoffeeType());
        preparedStatement.setString(2, order.getSize());
        preparedStatement.setString(3, order.getAddOns());
        preparedStatement.setDouble(4, order.getTotalPrice());

        preparedStatement.executeUpdate();
        System.out.println("Order saved to database.");
    } catch (SQLException e) {
        System.out.println("Error saving order to database: " + e.getMessage());
    }
}

    
    }

    

