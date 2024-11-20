package unabia1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PriceManager {

    public static void updatePrices(CoffeeDatabase coffeeDatabase, Scanner scanner) {
        System.out.println("\nCurrent Coffee Prices:");
        for (int i = 0; i < coffeeDatabase.getCoffeeMenu().size(); i++) {
            Coffee coffee = coffeeDatabase.getCoffeeMenu().get(i);
            System.out.println((i + 1) + ". " + coffee.getName() + " - $" + coffee.getPrice());
        }

        // Coffee choice validation
        int coffeeChoice = -1;
        while (coffeeChoice < 0 || coffeeChoice >= coffeeDatabase.getCoffeeMenu().size()) {
            System.out.print("Choose the coffee to update the price (1-" + coffeeDatabase.getCoffeeMenu().size() + "): ");
            try {
                coffeeChoice = scanner.nextInt() - 1;
                if (coffeeChoice < 0 || coffeeChoice >= coffeeDatabase.getCoffeeMenu().size()) {
                    System.out.println("Invalid coffee choice. Please choose a number between 1 and " + coffeeDatabase.getCoffeeMenu().size());
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the buffer
            }
        }

        // Price validation
        double newPrice = -1;
        while (newPrice < 0) {
            System.out.print("Enter the new price for " + coffeeDatabase.getCoffeeMenu().get(coffeeChoice).getName() + ": $");
            try {
                newPrice = scanner.nextDouble();
                if (newPrice < 0) {
                    System.out.println("Invalid price. Please enter a price greater than or equal to $0.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.nextLine(); // Clear the buffer
            }
        }

        // Update the in-memory data
        Coffee selectedCoffee = coffeeDatabase.getCoffeeMenu().get(coffeeChoice);
        selectedCoffee.setPrice(newPrice);

        // Save the change to the database
        updatePriceInDatabase(selectedCoffee.getName(), newPrice);

        System.out.println(selectedCoffee.getName() + " price updated to $" + newPrice);
    }

    private static void updatePriceInDatabase(String coffeeName, double newPrice) {
        String updateQuery = "UPDATE coffee_menu SET price = ? WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setString(2, coffeeName);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Price updated in database successfully.");
            } else {
                System.out.println("No record found for " + coffeeName + " in the database.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating price in database: " + e.getMessage());
        }
    }

  
}
