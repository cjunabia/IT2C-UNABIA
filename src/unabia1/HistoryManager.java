package unabia1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryManager {

    public static void viewHistory() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement historyStatement = connection.prepareStatement(
                     "SELECT * FROM purchase_history");
             PreparedStatement summaryStatement = connection.prepareStatement(
                     "SELECT coffee_type, COUNT(*) AS purchase_count FROM purchase_history GROUP BY coffee_type");
             ResultSet resultSet = historyStatement.executeQuery();
             ResultSet summaryResultSet = summaryStatement.executeQuery()) {

            // Display Purchase History
            System.out.println("\nPurchase History Report:");
            System.out.println("+-------------+------------------------+-------------------+-------------+-------------+-------------------+");
            System.out.println("| Order ID    | Coffee Type            | Size              | Add-ons     | Total Price | Purchase Date     |");
            System.out.println("+-------------+------------------------+-------------------+-------------+-------------+-------------------+");

            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                String coffeeType = resultSet.getString("coffee_type");
                String size = resultSet.getString("size");
                String addOns = resultSet.getString("add_ons");
                double totalPrice = resultSet.getDouble("total_price");
                String purchaseDate = resultSet.getString("date");

                System.out.printf("| %-11d | %-22s | %-17s | %-11s | %-11.2f | %-17s |\n", 
                                  orderId, coffeeType, size, addOns, totalPrice, purchaseDate);
            }

            System.out.println("+-------------+------------------------+-------------------+-------------+-------------+-------------------+");

            // Display Coffee Summary
            System.out.println("\nCoffee Purchase Summary:");
            System.out.println("+------------------------+------------------+");
            System.out.println("| Coffee Type            | Purchase Count  |");
            System.out.println("+------------------------+------------------+");

            while (summaryResultSet.next()) {
                String coffeeType = summaryResultSet.getString("coffee_type");
                int purchaseCount = summaryResultSet.getInt("purchase_count");

                System.out.printf("| %-22s | %-16d |\n", coffeeType, purchaseCount);
            }

            System.out.println("+------------------------+------------------+");

        } catch (SQLException e) {
            System.out.println("Error fetching purchase history: " + e.getMessage());
        }
    }
}
